package stream.reconfig.kirinmaru.android.ui.chapters

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import stream.reconfig.kirinmaru.android.databinding.ItemChapterBinding
import stream.reconfig.kirinmaru.android.parcel.NovelParcel
import stream.reconfig.kirinmaru.android.ui.common.fragment.DrawerRecyclerFragment
import stream.reconfig.kirinmaru.android.ui.navigation.FragmentNavigator
import stream.reconfig.kirinmaru.android.util.livedata.observeNonNull
import stream.reconfig.kirinmaru.android.util.offline.State
import stream.reconfig.kirinmaru.android.util.recycler.ItemDecorationUtil
import stream.reconfig.kirinmaru.android.util.viewmodel.ViewModelFactory
import stream.reconfig.kirinmaru.android.util.viewmodel.viewModel
import stream.reconfig.kirinmaru.core.NovelDetail
import javax.inject.Inject

/**
 *
 */
class ChaptersFragment : DrawerRecyclerFragment() {
  companion object {
    private const val FARGS_NOVEL = "novelParcel"
    @JvmStatic
    fun newInstance(novelParcel: NovelParcel): ChaptersFragment {
      return ChaptersFragment().apply {
        arguments = Bundle().apply { putParcelable(FARGS_NOVEL, novelParcel) }
      }
    }
  }

  @Inject
  lateinit var vmf: ViewModelFactory

  private val cvm by lazy { viewModel(vmf, ChaptersViewModel::class.java) }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val novelParcel: NovelParcel = arguments!!.getParcelable(FARGS_NOVEL)

    cvm.chapters.initNovel(novelParcel)
    binding.toolbar.title = novelParcel.novelTitle
    binding.refreshLayout.setOnRefreshListener { cvm.chapters.refresh() }

    val adapter = ChaptersAdapter(
        onClickItem = { chapterItem -> onClickItem(novelParcel, chapterItem) },
        onBind = ::onBindItem
    )
    with(binding.recyclerView) {
      addItemDecoration(ItemDecorationUtil.defaultVerticalDecor(resources))
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      setAdapter(adapter)
    }

    binding.root.post {
      cvm.chapters.observeNonNull(this) { adapter.updateData(it) }

      cvm.chapters.resourceState.observeNonNull(this) {
        with(binding.refreshLayout) {
          when (it.state) {
            State.COMPLETE -> isRefreshing = false
            State.LOADING -> isRefreshing = true
            State.ERROR -> {
              isRefreshing = false
              showSnackbar(it.message)
            }
          }
        }
      }
    }
  }

  private fun onBindItem(binding: ItemChapterBinding, list: MutableList<ChapterItem>, position: Int) {
    with(list[position]) {
      binding.title.text = taxonView
      binding.bookmark.visibility = if (currentRead) View.VISIBLE else View.GONE
    }
  }

  private fun onClickItem(novelItem: NovelDetail, chapterItem: ChapterItem) {
    activity?.let { FragmentNavigator.toReader(it, novelItem, chapterItem) }
  }

  private fun showSnackbar(message: String) {
    Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
  }
}

