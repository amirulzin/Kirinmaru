package stream.reconfig.kirinmaru.android.ui.common.recycler

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
abstract class SingleBindingAdapter<out C : Collection<*>, V : ViewDataBinding>(
    protected val collection: C,
    private inline val resourceId: Int,
    private inline val create: (binding: V) -> Unit = {},
    private inline val postCreate: (holder: BindingHolder<V>) -> Unit = {},
    private inline val bind: (binding: V, collection: C, position: Int) -> Unit = { _, _, _ -> throw NotImplementedError("ViewHolder bind block not defined") }
) : RecyclerView.Adapter<BindingHolder<V>>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<V> {
    val holder = BindingHolder(DataBindingUtil.inflate<V>(
        LayoutInflater.from(parent.context),
        resourceId,
        parent,
        false
    ), create)
    postCreate(holder)
    return holder
  }

  override fun getItemCount(): Int = collection.size

  override fun onBindViewHolder(holder: BindingHolder<V>, position: Int) {
    bind(holder.binding, collection, position)
    holder.binding.executePendingBindings()
  }
}

