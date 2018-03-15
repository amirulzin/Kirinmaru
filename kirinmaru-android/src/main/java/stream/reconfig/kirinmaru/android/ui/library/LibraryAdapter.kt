package stream.reconfig.kirinmaru.android.ui.library

import android.support.v7.util.DiffUtil
import io.reactivex.disposables.CompositeDisposable
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.ItemLibraryBinding
import stream.reconfig.kirinmaru.android.ui.common.recycler.SingleBindingAdapter
import stream.reconfig.kirinmaru.android.util.recycler.RxRecyclerUtil
import stream.reconfig.kirinmaru.android.util.rx.addTo

class LibraryAdapter(
    private val onClickItem: (LibraryItem) -> Unit,
    private val onClickCurrentRead: (LibraryItem) -> Unit,
    private val onClickLatest: (LibraryItem) -> Unit,
    private inline val onBind: (binding: ItemLibraryBinding, collection: MutableList<LibraryItem>, position: Int) -> Unit
) : SingleBindingAdapter<MutableList<LibraryItem>, ItemLibraryBinding>(
    collection = mutableListOf(),
    resourceId = R.layout.item_library,
    postCreate = { holder, collection ->
      with(holder.binding) {
        root.setOnClickListener { clickPredicate(holder, collection) { onClickItem(collection[it]) } }
        lastRead.setOnClickListener { clickPredicate(holder, collection) { onClickCurrentRead(collection[it]) } }
        latestChapter.setOnClickListener { clickPredicate(holder, collection) { onClickLatest(collection[it]) } }
      }
    },
    bind = onBind
) {

  private val disposables = CompositeDisposable()

  fun update(result: List<LibraryItem>) {
    RxRecyclerUtil.calcAndDispatchDiff(this) {
      object : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
          return collection.size
        }

        override fun getNewListSize(): Int {
          return result.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          return collection[oldItemPosition].novel.origin == result[newItemPosition].novel.origin
              && collection[oldItemPosition].novel.url == result[newItemPosition].novel.url

        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          return collection[oldItemPosition] == result[newItemPosition]
        }
      }
    }.subscribe { _ ->
      collection.clear()
      collection.addAll(result)
      disposables.clear()
    }.addTo(disposables)
  }
}