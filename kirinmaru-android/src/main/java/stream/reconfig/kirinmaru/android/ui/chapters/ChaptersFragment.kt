package stream.reconfig.kirinmaru.android.ui.chapters

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import commons.android.arch.ViewModelFactory
import commons.android.arch.observeNonNull
import commons.android.arch.offline.refresh.ResourceStateHandler
import commons.android.arch.viewModel
import commons.android.core.fragment.DrawerRecyclerFragment
import stream.reconfig.kirinmaru.android.databinding.ItemChapterBinding
import stream.reconfig.kirinmaru.android.parcel.NovelParcel
import stream.reconfig.kirinmaru.android.ui.navigation.FragmentNavigator
import stream.reconfig.kirinmaru.android.util.recycler.ItemDecorationUtil
import stream.reconfig.kirinmaru.core.NovelDetail
import javax.inject.Inject

/**
 *
 */
class ChaptersFragment : DrawerRecyclerFragment() {
  companion object {
    private const val FARGS_NOVEL = "novelParcel"

    @JvmStatic
    fun createArguments(novelParcel: NovelParcel) = Bundle().apply { putParcelable(FARGS_NOVEL, novelParcel) }

    @JvmStatic
    fun newInstance(novelParcel: NovelParcel): ChaptersFragment {
      return ChaptersFragment().apply { arguments = createArguments(novelParcel) }
    }
  }

  @Inject
  lateinit var vmf: ViewModelFactory

  private val cvm by lazy { viewModel(vmf, ChaptersViewModel::class.java) }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val novelParcel: NovelParcel = arguments!!.getParcelable(FARGS_NOVEL)!!

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
        ResourceStateHandler.handleStateUpdates(
          coordinatorLayout = binding.coordinatorLayout,
          refreshLayout = binding.refreshLayout,
          resourceState = it,
          remoteRefreshable = this
        )
      }
    }
  }

  private fun onBindItem(binding: ItemChapterBinding, list: MutableList<ChapterItem>, position: Int) {
    with(list[position]) {
      binding.title.text = title ?: taxonView
      binding.bookmark.visibility = if (currentRead) View.VISIBLE else View.GONE
    }
  }

  private fun onClickItem(novelItem: NovelDetail, chapterItem: ChapterItem) {
    activity?.let { FragmentNavigator.toReader(it, novelItem, chapterItem) }
  }
}

