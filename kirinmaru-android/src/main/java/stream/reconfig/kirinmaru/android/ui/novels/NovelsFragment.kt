package stream.reconfig.kirinmaru.android.ui.novels

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import commons.android.arch.ViewModelFactory
import commons.android.arch.observe
import commons.android.arch.observeNonNull
import commons.android.arch.offline.refresh.ResourceStateHandler
import commons.android.arch.viewModel
import commons.android.core.fragment.DrawerRecyclerFragment
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.ItemNovelBinding
import stream.reconfig.kirinmaru.android.ui.navigation.FragmentNavigator
import stream.reconfig.kirinmaru.android.util.recycler.ItemDecorationUtil
import stream.reconfig.kirinmaru.plugins.PluginMap
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
  lateinit var pluginMap: PluginMap

  @Inject
  lateinit var vmf: ViewModelFactory

  private val nvm by lazy { viewModel(vmf, NovelsViewModel::class.java) }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    getToolbar().contentInsetStartWithNavigation = 0

    val origin = arguments!!.getString(FARGS_ORIGIN)!!

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
      with(binding.toolbarSpinner) {
        visibility = View.VISIBLE
        val list = pluginMap.keys.toList()
        adapter = ArrayAdapter<String>(context, R.layout.item_spinner, R.id.textView, list)
        setSelection(list.indexOf(origin))
        onItemSelectedListener = onSpinnerItem(list)
      }

      nvm.novels.observe(this) {
        novelAdapter.updateData(it ?: emptyList())
      }

      nvm.novels.resourceState.observeNonNull(this) {
        ResourceStateHandler.handleStateUpdates(
          coordinatorLayout = binding.coordinatorLayout,
          refreshLayout = binding.refreshLayout,
          resourceState = it,
          remoteRefreshable = this
        )
      }
    }
  }

  private fun onSpinnerItem(list: List<String>): AdapterView.OnItemSelectedListener {
    return object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {}

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val newOrigin = list[position]
        nvm.novels.initOrigin(newOrigin)
        arguments = Bundle().apply {
          putString(FARGS_ORIGIN, newOrigin)
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

  private fun onClickNovel(novelItem: NovelItem) {
    FragmentNavigator.toChapters(activity!!, novelItem)
  }

  private fun onToggleFavorite(novelItem: NovelItem, isChecked: Boolean) {
    nvm.novels.toggleFavorite(novelItem, isChecked)
  }

  private fun displayTags(set: Set<String>): String {
    val sb = StringBuilder()
    for ((index, s) in set.withIndex()) {
      sb.append(s)
      if (set.size - index > 1) sb.append(" | ")
    }
    return sb.toString()
  }
}