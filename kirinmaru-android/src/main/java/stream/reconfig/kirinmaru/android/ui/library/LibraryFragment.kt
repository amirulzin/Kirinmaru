package stream.reconfig.kirinmaru.android.ui.library

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.logd
import stream.reconfig.kirinmaru.android.ui.common.fragment.DrawerRecyclerFragment
import stream.reconfig.kirinmaru.android.ui.navigation.FragmentNavigator
import stream.reconfig.kirinmaru.android.util.livedata.observe
import stream.reconfig.kirinmaru.android.util.offline.State
import stream.reconfig.kirinmaru.android.util.recycler.ItemDecorationUtil
import stream.reconfig.kirinmaru.android.util.viewmodel.ViewModelFactory
import stream.reconfig.kirinmaru.android.util.viewmodel.viewModel
import javax.inject.Inject

class LibraryFragment : DrawerRecyclerFragment() {
  companion object {
    fun newInstance() = LibraryFragment()
  }

  @Inject
  lateinit var vmf: ViewModelFactory

  private val lvm by lazy { viewModel(vmf, LibraryViewModel::class.java) }

  private val updatedColor by lazy { ContextCompat.getColor(context!!, R.color.colorAccentSecondary) }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    getToolbar().title = "Library"

    val adapter = LibraryAdapter(
        onClickItem = { libraryItem ->
          activity?.let { FragmentNavigator.toChapters(it, libraryItem.novel) }
        },
        onClickCurrentRead = { libraryItem ->
          activity?.let { libraryItem.currentRead?.run { FragmentNavigator.toReader(it, libraryItem.novel, this) } }
        },
        onClickLatest = { libraryItem ->
          activity?.let { libraryItem.latest?.run { FragmentNavigator.toReader(it, libraryItem.novel, this) } }
        },
        onBind = { binding, collection, position ->
          with(collection[position]) {
            binding.title.text = novel.novelTitle
            binding.latestChapter.run {
              text = latest?.taxonView.also { visibility = if (it == null) View.GONE else View.VISIBLE }
            }
            binding.lastRead.run {
              text = currentRead?.taxonView.also { visibility = if (it == null) View.GONE else View.VISIBLE }
            }
            binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
            logd("Updated : $isUpdated for ${novel.url}")
            if (isUpdated)
              setUpdatedColor(binding.latestChapter)
          }
        }
    )

    with(binding.recyclerView) {
      addItemDecoration(ItemDecorationUtil.defaultVerticalDecor(resources))
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      setAdapter(adapter)
    }

    binding.refreshLayout.setOnRefreshListener { lvm.library.refresh() }

    binding.root.post {
      lvm.library.resourceState.observe(this) {
        it?.let {
          binding.refreshLayout.isRefreshing = it.state == State.LOADING
          if (it.state == State.ERROR) showSnackBar(it.message)
        }
      }

      lvm.library.observe(this) {
        it?.let { adapter.update(it) }
      }
    }
  }

  private fun setUpdatedColor(textView: TextView) {
    textView.setTextColor(updatedColor)
  }

  private fun showSnackBar(message: String) {
    Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
  }
}