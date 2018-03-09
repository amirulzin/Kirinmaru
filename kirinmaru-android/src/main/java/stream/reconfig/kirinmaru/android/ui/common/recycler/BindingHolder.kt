package stream.reconfig.kirinmaru.android.ui.common.recycler

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

data class BindingHolder<out V : ViewDataBinding>(
    val binding: V,
    private inline val create: (V) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
  init {
    create(binding)
  }
}