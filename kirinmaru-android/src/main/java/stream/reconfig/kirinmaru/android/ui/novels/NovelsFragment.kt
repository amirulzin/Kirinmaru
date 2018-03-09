package stream.reconfig.kirinmaru.android.ui.novels

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import stream.reconfig.kirinmaru.android.databinding.ItemNovelBinding
import stream.reconfig.kirinmaru.android.ui.common.fragment.DrawerRecyclerFragment
import stream.reconfig.kirinmaru.android.ui.navigation.FragmentNavigator
import stream.reconfig.kirinmaru.android.util.livedata.observe
import stream.reconfig.kirinmaru.android.util.offline.State
import stream.reconfig.kirinmaru.android.util.recycler.ItemDecorationUtil
import stream.reconfig.kirinmaru.android.util.viewmodel.ViewModelFactory
import stream.reconfig.kirinmaru.android.util.viewmodel.viewModel
import javax.inject.Inject

/**
 * Display Novels in list
 */
class NovelsFragment : DrawerRecyclerFragment() {

  companion object {
    private const val FARGS_ORIGIN = "origin"
    @JvmStatic
    fun newInstance(origin: String): NovelsFragment {
      return NovelsFragment().apply {
        arguments = Bundle().apply {
          putString(FARGS_ORIGIN, origin)
        }
      }
    }
  }

  @Inject
  lateinit var vmf: ViewModelFactory

  private val nvm by lazy { viewModel(vmf, NovelsViewModel::class.java) }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    val origin = arguments!!.getString(FARGS_ORIGIN)!!
    binding.toolbar.title = origin

    val novelAdapter = NovelAdapter(
        onClickNovel = ::onClickNovel,
        onToggleFavorite = ::onToggleFavorite,
        onBind = ::onBindItem
    )

    with(binding.recyclerView) {
      addItemDecoration(ItemDecorationUtil.defaultVerticalDecor(resources))
      layoutManager = LinearLayoutManager(context)
      adapter = novelAdapter
    }

    with(nvm.novels) {
      binding.refreshLayout.setOnRefreshListener { refresh() }
      initOrigin(origin)
    }

    binding.root.post {
      nvm.novels.observe(this) {
        it?.let { novelAdapter.updateData(it) }
      }

      nvm.novels.resourceState.observe(this) {
        it?.let {
          with(binding.refreshLayout) {
            when (it.state) {
              State.COMPLETE -> isRefreshing = false
              State.LOADING -> isRefreshing = true
              State.ERROR -> {
                isRefreshing = false
                val message = it.message
                showSnackbar(message)
              }
            }
          }
        }
      }
    }
  }

  private fun showSnackbar(message: String) {
    Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT)
        .show()
  }

  private fun onBindItem(binding: ItemNovelBinding, list: MutableList<NovelItem>, position: Int) {
    val novelId = list[position]
    with(binding) {
      title.text = novelId.novelTitle
      favorite.isChecked = novelId.isFavorite
      if (novelId.tags.isEmpty()) category.visibility = View.GONE
      else {
        category.text = displayTags(novelId.tags)
        if (category.visibility == View.GONE) category.visibility = View.VISIBLE
      }
    }
  }

  private fun onClickNovel(novelItem: NovelItem) =
      FragmentNavigator.toChapters(activity!!, novelItem)

  private fun onToggleFavorite(novelItem: NovelItem, isChecked: Boolean) =
      nvm.novels.toggleFavorite(novelItem, isChecked)

  private fun displayTags(set: Set<String>): String {
    val sb = StringBuilder()
    for ((index, s) in set.withIndex()) {
      sb.append(s)
      if (set.size - index > 1) sb.append(" | ")
    }
    return sb.toString()
  }
}