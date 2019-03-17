package commons.android.core.recycler

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

data class BindingHolder<out V : ViewDataBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)