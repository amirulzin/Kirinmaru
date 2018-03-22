package stream.reconfig.kirinmaru.android.ui.reader

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.assets.Fonts
import stream.reconfig.kirinmaru.android.databinding.FragmentReaderBinding
import stream.reconfig.kirinmaru.android.databinding.ViewReaderbarBinding
import stream.reconfig.kirinmaru.android.logd
import stream.reconfig.kirinmaru.android.ui.common.fragment.DatabindingFragment
import stream.reconfig.kirinmaru.android.util.fullscreen.FullScreenUtil.enterFullscreen
import stream.reconfig.kirinmaru.android.util.fullscreen.FullScreenUtil.exitFullScreen
import stream.reconfig.kirinmaru.android.util.intent.IntentFactory
import stream.reconfig.kirinmaru.android.util.livedata.observe
import stream.reconfig.kirinmaru.android.util.livedata.observeNonNull
import stream.reconfig.kirinmaru.android.util.offline.State.*
import stream.reconfig.kirinmaru.android.util.viewmodel.ViewModelFactory
import stream.reconfig.kirinmaru.android.util.viewmodel.viewModel
import javax.inject.Inject


class ReaderFragment : DatabindingFragment<FragmentReaderBinding>() {
  companion object {
    private const val FARGS_READER = "readerData"
    @JvmStatic
    fun newInstance(readerParcel: ReaderParcel): ReaderFragment {
      return ReaderFragment().apply {
        arguments = Bundle().apply {
          putParcelable(FARGS_READER, readerParcel)
        }
      }
    }
  }

  @Inject
  lateinit var fonts: Fonts

  @Inject
  lateinit var vmf: ViewModelFactory

  private val rvm by lazy { viewModel(vmf, ReaderViewModel::class.java) }

  override val layoutId = R.layout.fragment_reader

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val readerParcel: ReaderParcel = arguments!!.getParcelable(FARGS_READER)!!
    rvm.reader.initReaderData(readerParcel)
    binding.refreshLayout.setColorSchemeColors(
        ContextCompat.getColor(context!!, R.color.colorAccent),
        ContextCompat.getColor(context!!, R.color.colorPrimary)
    )

    binding.refreshLayout.setOnRefreshListener {
      rvm.reader.refresh()
    }

    binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
      handleBottomBarVisibility(verticalOffset, appBarLayout)
    }

    rvm.reader.observe(this) {
      it?.let {
        updateReaderBar(it)
        if (it.hasText())
          binding.rawText.setText(it.text, TextView.BufferType.SPANNABLE)
      } ?: when (rvm.reader.resourceState.value?.state) {
        ERROR -> binding.rawText.text = "Chapter can't be retrieved. Try refreshing"
        else -> binding.rawText.text = ""
      }
      binding.refreshLayout.isRefreshing = false
    }

    rvm.reader.resourceState.observe(this) { resourceState ->
      with(binding.refreshLayout) {
        resourceState?.apply {
          when (this.state) {
            COMPLETE -> isRefreshing = false
            LOADING -> isRefreshing = true
            ERROR -> {
              isRefreshing = false
              showSnackbar(this.message)
            }
          }
        } ?: run { isRefreshing = false }
      }
    }

    rvm.readerSetting.observeNonNull(this) { readerSetting ->
      logd(readerSetting.toString())
      createFontView(readerSetting)
      with(binding) {
        rawText.apply {
          setTextColor(readerSetting.fontColor)
          letterSpacing = readerSetting.letterSpacingSp / 100f
          setLineSpacing(readerSetting.lineSpacingExtra.toFloat(), 1f)
          textSize = readerSetting.fontSizeSp.toFloat()
          typeface = fonts.toTypeface(readerSetting.fontName)
        }
        coordinatorLayout.setBackgroundColor(readerSetting.backgroundColor)
      }
      showSnackbar("New reader setting applied")
    }

    bindReaderBar(binding.buttonBarTop)
    bindReaderBar(binding.buttonBarBottom)
    hideFontMenu()
  }

  private fun bindReaderBar(binding: ViewReaderbarBinding) {
    with(binding) {
      readerFont.setOnClickListener { showFontMenu() }
      readerNext.setOnClickListener { rvm.reader.navigateNext() }
      readerPrevious.setOnClickListener { rvm.reader.navigatePrevious() }
      readerBrowser.setOnClickListener {
        rvm.reader.absoluteUrl()?.let { activity?.startActivity(IntentFactory.createBrowserIntent(it)) }
      }
    }
  }

  private fun updateReaderBar(it: ReaderDetail) {
    setReaderBarTitle(it.taxon, binding.buttonBarTop)
    setReaderBarTitle(it.taxon, binding.buttonBarBottom)
    setReaderBarNavigation(it, binding.buttonBarTop)
    setReaderBarNavigation(it, binding.buttonBarBottom)
  }


  private fun setReaderBarTitle(title: String, binding: ViewReaderbarBinding) {
    binding.title.text = title
  }

  private fun setReaderBarNavigation(readerDetail: ReaderDetail, binding: ViewReaderbarBinding) {
    binding.readerNext.isEnabled = readerDetail.canNavigateNext()
    binding.readerPrevious.isEnabled = readerDetail.canNavigatePrevious()
  }

  private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.readerSettingParent) }

  private fun showFontMenu() {
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
  }

  private fun hideFontMenu() {
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
  }

  private fun createFontView(readerSetting: ReaderSetting) {
    ReaderSettingHelper(
        fragmentManager = activity!!.fragmentManager,
        binding = binding.readerSetting,
        readerSetting = readerSetting,
        fonts = fonts.list,
        listener = object : ReaderSettingHelper.Listener {
          override fun onComplete(readerSetting: ReaderSetting) {
            rvm.readerSetting.postValue(readerSetting)
            hideFontMenu()
          }

          override fun onCancel() {
            hideFontMenu()
          }
        }
    )
  }

  private fun handleBottomBarVisibility(verticalOffset: Int, appBarLayout: AppBarLayout) {
    val visibility = binding.buttonBarBottom.container.visibility
    if (visibility == View.VISIBLE && verticalOffset == 0)
      binding.buttonBarBottom.container.visibility = View.INVISIBLE
    else if (visibility == View.INVISIBLE
        && appBarLayout.height + verticalOffset == 0
        && rvm.reader.value?.hasText() != null) binding.buttonBarBottom.container.visibility = View.VISIBLE
  }

  private fun showSnackbar(message: String) {
    Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_LONG)
        .show()
  }

  override fun onStart() {
    super.onStart()
    binding.root.post { enterFullscreen(activity) }
    activity?.window?.decorView?.background = null
  }

  override fun onStop() {
    super.onStop()
    exitFullScreen(activity)
    activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorWindowBackground))
  }
}
