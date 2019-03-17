package stream.reconfig.kirinmaru.android.ui.chapters

import android.support.v7.util.DiffUtil
import commons.android.core.recycler.SingleBindingAdapter
import commons.android.rx.RxRecyclerUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.ItemChapterBinding

/**
 * Adapter for ChapterItem. Data updates will clear the original mutable list
 * before they are added to maintain immutability on the list reference
 */
class ChaptersAdapter(
  private inline val onClickItem: (ChapterItem) -> Unit,
  private inline val onBind: (binding: ItemChapterBinding, list: MutableList<ChapterItem>, position: Int) -> Unit
) : SingleBindingAdapter<MutableList<ChapterItem>, ItemChapterBinding>(
  collection = mutableListOf(),
  resourceId = R.layout.item_chapter,
  postCreate = { holder, collection ->
    holder.binding.root.setOnClickListener {
      clickPredicate(holder, collection) { onClickItem(collection[it]) }
    }
  },
  bind = onBind
) {

  private val disposables = CompositeDisposable()

  fun updateData(result: List<ChapterItem>) {
    RxRecyclerUtil.calcAndDispatchDiff(this) {
      object : DiffUtil.Callback() {

        override fun getOldListSize() = collection.size

        override fun getNewListSize() = result.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          collection[oldItemPosition].url == result[newItemPosition].url

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          collection[oldItemPosition].url == result[newItemPosition].url
      }
    }.subscribe { _, _ ->
      collection.clear()
      collection.addAll(result)
      disposables.clear()
    }.addTo(disposables)
  }
}