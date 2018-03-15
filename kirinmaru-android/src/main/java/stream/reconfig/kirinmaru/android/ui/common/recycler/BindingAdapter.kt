package stream.reconfig.kirinmaru.android.ui.common.recycler

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 *  An attempt on multiple view type DataBinding based RecyclerView adapter
 */
open class BindingAdapter<out C : Collection<*>>(
    protected val collection: C,
    private inline val resourceId: (viewType: Int) -> Int = { throw NotImplementedError("No layout resource id being returned") },
    private inline val postCreate: (holder: BindingHolder<*>) -> Unit = {},
    private inline val bind: (binding: ViewDataBinding, collection: C, position: Int) -> Unit = { _, _, _ -> throw NotImplementedError("ViewHolder bind block not defined") }
) : RecyclerView.Adapter<BindingHolder<*>>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<*> {
    val holder = BindingHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), resourceId(viewType), parent, false))
    postCreate(holder)
    return holder
  }

  override fun getItemCount(): Int = collection.size

  override fun onBindViewHolder(holder: BindingHolder<*>, position: Int) {
    bind(holder.binding, collection, position)
    holder.binding.executePendingBindings()
  }
}

