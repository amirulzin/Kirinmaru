package stream.reconfig.kirinmaru.android.ui.chapters

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import stream.reconfig.kirinmaru.android.ui.common.fragment.DrawerRecyclerFragment
import stream.reconfig.kirinmaru.android.ui.novels.NovelItem
import stream.reconfig.kirinmaru.android.ui.taxonomy.Taxonomy
import stream.reconfig.kirinmaru.android.util.livedata.observe
import stream.reconfig.kirinmaru.android.util.offline.State
import stream.reconfig.kirinmaru.android.util.recycler.ItemDecorationUtil
import stream.reconfig.kirinmaru.android.util.viewmodel.ViewModelFactory
import stream.reconfig.kirinmaru.android.util.viewmodel.viewModel
import javax.inject.Inject

/**
 *
 */
class ChaptersFragment : DrawerRecyclerFragment() {
  companion object {
    private const val FARGS_NOVEL = "novelItem"

    @JvmStatic
    fun newInstance(novelItem: NovelItem): ChaptersFragment {
      return ChaptersFragment().apply {
        arguments = Bundle().apply { putParcelable(FARGS_NOVEL, novelItem) }
      }
    }
  }

  @Inject
  lateinit var vmf: ViewModelFactory

  private val cvm by lazy { viewModel(vmf, ChaptersViewModel::class.java) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val novelItem: NovelItem = arguments!!.getParcelable(FARGS_NOVEL)

    cvm.chapters.initNovel(novelItem)
    binding.toolbar.title = novelItem.novelTitle
    binding.refreshLayout.setOnRefreshListener { cvm.chapters.refresh() }

    val adapter = ChaptersAdapter(
        onClickItem = {

        },
        onBind = { binding, list, position ->
          val item = list[position]
          binding.title.text = Taxonomy.View.getDisplayable(item.taxon)
          //binding.bookmark
        }
    )
    with(binding.recyclerView) {
      addItemDecoration(ItemDecorationUtil.defaultVerticalDecor(resources))
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      setAdapter(adapter)
    }

    view.post {
      observe(adapter)
    }
  }

  private fun observe(adapter: ChaptersAdapter) {
    cvm.chapters.observe(this) {
      it?.let {
        adapter.updateData(it)
      }
    }

    cvm.chapters.resourceState.observe(this) {
      it?.let {
        with(binding.refreshLayout) {
          when (it.state) {
            State.LOADING -> isRefreshing = true
            State.COMPLETE -> isRefreshing = false
            State.ERROR -> {
              isRefreshing = false
              Snackbar.make(binding.coordinatorLayout, it.message, Snackbar.LENGTH_SHORT).show()
            }
          }
        }
      }
    }
  }
}

