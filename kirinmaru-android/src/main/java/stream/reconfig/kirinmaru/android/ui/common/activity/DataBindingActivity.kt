package stream.reconfig.kirinmaru.android.ui.common.activity

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import dagger.android.support.DaggerAppCompatActivity

/**
 * Base DataBindingActivity. Invoke [bindContentView] to set the view contents
 */
abstract class DataBindingActivity<V : ViewDataBinding> : DaggerAppCompatActivity() {
  protected lateinit var binding: V
  protected abstract val layoutId: Int

  protected fun bindContentView(): V {
    binding = DataBindingUtil.setContentView(this, layoutId)
    return binding
  }
}