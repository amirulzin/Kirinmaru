package stream.reconfig.kirinmaru.android.ui.chapters

import android.support.v7.util.DiffUtil
import io.reactivex.disposables.CompositeDisposable
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.ItemChapterBinding
import stream.reconfig.kirinmaru.android.ui.common.recycler.SingleBindingAdapter
import stream.reconfig.kirinmaru.android.util.recycler.RxRecyclerUtil
import stream.reconfig.kirinmaru.android.util.rx.addTo

/**
 * Adapter for ChapterItem. Data updates will clear the original mutable list
 * before they are added to maintain immutability on the list reference
 */
class ChaptersAdapter(
    private val list: MutableList<ChapterItem> = mutableListOf(),
    private inline val onClickItem: (ChapterItem) -> Unit,
    private inline val onBind: (binding: ItemChapterBinding, list: MutableList<ChapterItem>, position: Int) -> Unit
) : SingleBindingAdapter<MutableList<ChapterItem>, ItemChapterBinding>(
    collection = list,
    resourceId = R.layout.item_chapter,
    postCreate = { holder ->
      holder.binding.root.setOnClickListener {
        val i = holder.adapterPosition
        if (i >= 0 && i < list.size) {
          onClickItem(list[i])
        }
      }
    },
    bind = onBind
) {

  private val compositeDisposable = CompositeDisposable()

  fun updateData(result: List<ChapterItem>) {
    compositeDisposable.clear()
    val callback = object : DiffUtil.Callback() {

      override fun getOldListSize() = list.size

      override fun getNewListSize() = result.size

      override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          list[oldItemPosition].url == result[newItemPosition].url

      override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          list[oldItemPosition].url == result[newItemPosition].url
    }
    list.clear()
    list.addAll(result)
    RxRecyclerUtil.calcAndDispatchDiff(this, callback)
        .addTo(compositeDisposable)
  }
}