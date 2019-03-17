package stream.reconfig.kirinmaru.android.ui.novels

import android.support.v7.util.DiffUtil
import commons.android.core.recycler.SingleBindingAdapter
import commons.android.rx.RxRecyclerUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.ItemNovelBinding

@Suppress("UNCHECKED_CAST")
class NovelAdapter(
  private inline val onClickNovel: (NovelItem) -> Unit,
  private inline val onToggleFavorite: (NovelItem, isChecked: Boolean) -> Unit,
  private inline val onBind: (binding: ItemNovelBinding, list: MutableList<NovelItem>, position: Int) -> Unit
) : SingleBindingAdapter<MutableList<NovelItem>, ItemNovelBinding>(
  collection = mutableListOf(),
  resourceId = R.layout.item_novel,
  bind = onBind,
  postCreate = { holder, collection ->
    with(holder.binding) {
      root.setOnClickListener {
        clickPredicate(holder, collection) { onClickNovel(collection[it]) }
      }
      favorite.setOnCheckedChangeListener { _, isChecked ->
        clickPredicate(holder, collection) { onToggleFavorite(collection[it], isChecked) }
      }
    }
  }
) {

  private val disposables = CompositeDisposable()

  fun updateData(result: List<NovelItem>) {
    RxRecyclerUtil.calcAndDispatchDiff(this) {
      object : DiffUtil.Callback() {

        override fun getOldListSize() = collection.size

        override fun getNewListSize() = result.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          collection[oldItemPosition].url == result[newItemPosition].url

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          collection[oldItemPosition] == result[newItemPosition]
      }
    }.subscribe { _, _ ->
      collection.clear()
      collection.addAll(result)
      disposables.clear()
    }.addTo(disposables)
  }
}