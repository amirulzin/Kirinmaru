package stream.reconfig.kirinmaru.android.ui.novels

import android.support.v7.util.DiffUtil
import io.reactivex.disposables.CompositeDisposable
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.ItemNovelBinding
import stream.reconfig.kirinmaru.android.ui.common.recycler.SingleBindingAdapter
import stream.reconfig.kirinmaru.android.util.recycler.RxRecyclerUtil
import stream.reconfig.kirinmaru.android.util.rx.addTo

@Suppress("UNCHECKED_CAST")
class NovelAdapter(
    private val list: MutableList<NovelItem> = mutableListOf(),
    private inline val onClickNovel: (NovelItem) -> Unit,
    private inline val onToggleFavorite: (NovelItem, isChecked: Boolean) -> Unit,
    private inline val onBind: (binding: ItemNovelBinding, list: MutableList<NovelItem>, position: Int) -> Unit
) : SingleBindingAdapter<MutableList<NovelItem>, ItemNovelBinding>(

    collection = list,
    resourceId = R.layout.item_novel,
    bind = onBind,
    postCreate = { holder ->
      val b = holder.binding
      b.root.setOnClickListener {
        val i = holder.adapterPosition
        if (i >= 0 && i < list.size) {
          onClickNovel(list[i])
        }
      }

      b.favorite.setOnCheckedChangeListener { _, isChecked ->
        val i = holder.adapterPosition
        if (i >= 0 && i < list.size) {
          onToggleFavorite(list[i], isChecked)
        }
      }
    }
) {

  private val compositeDisposable = CompositeDisposable()

  fun updateData(result: List<NovelItem>) {
    compositeDisposable.clear()

    val callback = object : DiffUtil.Callback() {

      override fun getOldListSize() = list.size

      override fun getNewListSize() = result.size

      override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          result[oldItemPosition].url == list[newItemPosition].url

      override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          result[oldItemPosition] == list[newItemPosition]

    }

    list.clear()
    list.addAll(result)
    RxRecyclerUtil.calcAndDispatchDiff(this, callback)
        .addTo(compositeDisposable)
  }
}