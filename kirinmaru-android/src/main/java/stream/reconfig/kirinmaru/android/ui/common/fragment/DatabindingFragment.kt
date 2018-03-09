package stream.reconfig.kirinmaru.android.ui.common.fragment

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment

/**
 * Abstract fragment with ViewDataBinding helper
 */
abstract class DatabindingFragment<V : ViewDataBinding> : DaggerFragment() {
  protected lateinit var binding: V
  protected abstract val layoutId: Int

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
    return binding.root
  }
}