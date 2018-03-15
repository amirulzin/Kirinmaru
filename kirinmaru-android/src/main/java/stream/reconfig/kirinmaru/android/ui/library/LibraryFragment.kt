package stream.reconfig.kirinmaru.android.ui.library

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import stream.reconfig.kirinmaru.android.logd
import stream.reconfig.kirinmaru.android.ui.common.fragment.DrawerRecyclerFragment
import stream.reconfig.kirinmaru.android.ui.navigation.FragmentNavigator
import stream.reconfig.kirinmaru.android.util.livedata.observeNonNull
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
            binding.latestChapter.text = latest?.taxonView
            binding.lastRead.text = currentRead?.taxonView
            binding.loadingView.visibility = View.GONE
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
      lvm.library.resourceState.observeNonNull(this) {
        logd("State Received: $it")
        binding.refreshLayout.isRefreshing = it.state == State.LOADING
        if (it.state == State.ERROR) showSnackBar(it.message)
      }

      lvm.library.observeNonNull(this) {
        logd("Library Received: $it")
        adapter.update(it)
      }
    }
  }

  private fun showSnackBar(message: String) {
    Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
  }
}