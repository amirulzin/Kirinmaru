package stream.reconfig.kirinmaru.android.ui.common.fragment

import android.databinding.ViewDataBinding
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.ui.common.activity.DrawerBindingActivity
import java.util.concurrent.atomic.AtomicReference

abstract class DrawerFragment<V : ViewDataBinding> : DatabindingFragment<V>() {

  protected abstract fun getToolbar(): Toolbar

  private val drawerToggle = AtomicReference<ActionBarDrawerToggle>()

  override fun onResume() {
    super.onResume()
    if (activity is DrawerBindingActivity) {
      view?.let {
        it.post {
          (activity as DrawerBindingActivity).apply {
            val toggle = ActionBarDrawerToggle(
                activity,
                getDrawerLayout(),
                getToolbar(),
                R.string.drawer_open,
                R.string.drawer_close
            )
            drawerToggle.set(toggle)
            drawerToggle.get().syncState()
            getDrawerLayout().addDrawerListener(drawerToggle.get())
          }
        }
      }
    }
  }

  override fun onPause() {
    super.onPause()
    if (activity is DrawerBindingActivity) {
      if (drawerToggle.get() != null) {
        (activity as DrawerBindingActivity).getDrawerLayout()
            .removeDrawerListener(drawerToggle.get())
      }
      drawerToggle.set(null)
    }
  }
}
