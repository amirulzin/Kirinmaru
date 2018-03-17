package stream.reconfig.kirinmaru.android.ui.reader

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.view.View
import android.widget.TextView
import stream.reconfig.kirinmaru.android.R
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

    // Semi-intended workaround for an unknown SwipeRefreshLayout bug.
    // Apparently it is not inflating properly unless some calls set it to 'dirty'.
    // Currently unable to reproduce this bug in silo.
    // TODO: Fix SwipeRefreshLayout inflation bug (remove the block when fixed)
//    binding.refreshLayout.post {
//      binding.refreshLayout.isRefreshing = true
//    }

    binding.refreshLayout.setOnRefreshListener {
      rvm.reader.refresh()
    }

    binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
      handleBottomBarVisibility(verticalOffset, appBarLayout)
    }

    rvm.reader.observe(this) {
      it?.let {
        if (it.hasText())
          binding.rawText.setText(it.text, TextView.BufferType.SPANNABLE)
        setReaderBarNavigation(it, binding.buttonBarTop!!)
        setReaderBarNavigation(it, binding.buttonBarBottom!!)
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

    bindReaderBar(binding.buttonBarTop!!)
    bindReaderBar(binding.buttonBarBottom!!)
  }

  private fun bindReaderBar(binding: ViewReaderbarBinding) {
    rvm.readerSetting.observeNonNull(this) { readerSetting ->
      logd("READER SETTING LOADED")
      with(binding) {
        readerFont.setOnClickListener {
          showFontMenu(it, readerSetting)
        }

        readerNext.setOnClickListener { rvm.reader.navigateNext() }
        readerPrevious.setOnClickListener { rvm.reader.navigatePrevious() }
        readerBrowser.setOnClickListener {
          rvm.reader.absoluteUrl()?.let { activity?.startActivity(IntentFactory.createBrowserIntent(it)) }
        }
      }
    }
  }

  private fun setReaderBarNavigation(readerDetail: ReaderDetail, binding: ViewReaderbarBinding) {
    binding.readerNext.isEnabled = readerDetail.canNavigateNext()
    binding.readerPrevious.isEnabled = readerDetail.canNavigatePrevious()
  }

  private fun showFontMenu(view: View, readerSetting: ReaderSetting) {
    createMenu(view).show()
  }

  private fun createMenu(view: View): PopupMenu {
    return PopupMenu(context!!, view).apply {
      inflate(R.menu.drawer_menu)
    }
  }

  private fun handleBottomBarVisibility(verticalOffset: Int, appBarLayout: AppBarLayout) {
    val visibility = binding.buttonBarBottom?.container?.visibility
    if (visibility == View.VISIBLE && verticalOffset == 0)
      binding.buttonBarBottom?.container?.visibility = View.INVISIBLE
    else if (visibility == View.INVISIBLE
        && appBarLayout.height + verticalOffset == 0
        && rvm.reader.value?.hasText() != null) binding.buttonBarBottom?.container?.visibility = View.VISIBLE
  }

  private fun showSnackbar(message: String) {
    Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_LONG)
        .show()
  }

  override fun onStart() {
    super.onStart()
    binding.root.post { enterFullscreen(activity) }
  }

  override fun onStop() {
    super.onStop()
    exitFullScreen(activity)
  }
}
