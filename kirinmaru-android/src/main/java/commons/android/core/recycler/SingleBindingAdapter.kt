package commons.android.core.recycler

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 *  DataBinding based RecyclerView adapter with single type of ViewHolder.
 *
 *  More efficient than [BindingAdapter] due to calls do not use manual type checking.
 */
open class SingleBindingAdapter<out C : Collection<*>, V : ViewDataBinding>(
  @Suppress("MemberVisibilityCanBePrivate") protected val collection: C,
  private inline val resourceId: Int,
  private inline val postCreate: (holder: BindingHolder<V>, collection: C) -> Unit = { _, _ -> },
  private inline val bind: (binding: V, collection: C, position: Int) -> Unit = { _, _, _ -> throw NotImplementedError("ViewHolder bind block not defined") }
) : RecyclerView.Adapter<BindingHolder<V>>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<V> {
    val holder = BindingHolder(DataBindingUtil.inflate<V>(LayoutInflater.from(parent.context), resourceId, parent, false))
    postCreate(holder, collection)
    return holder
  }

  override fun getItemCount(): Int = collection.size

  override fun onBindViewHolder(holder: BindingHolder<V>, position: Int) {
    bind(holder.binding, collection, position)
    holder.binding.executePendingBindings()
  }

  companion object {
    @JvmStatic
    inline fun <C : Collection<*>, V : ViewDataBinding> clickPredicate(
      holder: BindingHolder<V>,
      collection: C,
      behavior: (pos: Int) -> Unit
    ) {
      val i = holder.adapterPosition
      if (i >= 0 && i < collection.size) {
        behavior(i)
      }
    }
  }
}

