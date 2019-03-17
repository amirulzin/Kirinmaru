package commons.android.core.fragment

import android.databinding.ViewDataBinding
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import commons.android.ProjectConstants
import commons.android.core.activity.DrawerActivity
import java.util.concurrent.atomic.AtomicReference

abstract class DrawerFragment<V : ViewDataBinding> : DataBindingFragment<V>() {

  protected open val sidebarEnabled = true
  protected open val bottomNavEnabled = true

  protected open fun getToolbar(): Toolbar? = null

  private val drawerToggle = AtomicReference<ActionBarDrawerToggle>()

  protected fun getDrawerActivity(): DrawerActivity? {
    return activity?.takeIf { activity is DrawerActivity }?.let { it as DrawerActivity }
  }

  override fun onResume() {
    super.onResume()
    activity?.takeIf { activity is DrawerActivity }?.let { activity ->
      with(activity as DrawerActivity) {

        toggleSidebar(sidebarEnabled)
        toggleBottomNav(bottomNavEnabled)

        if (sidebarEnabled) {
          getToolbar()?.let { toolbar ->
            view?.post {
              apply {
                ActionBarDrawerToggle(
                  activity,
                  getDrawerLayout(),
                  toolbar,
                  ProjectConstants.STR_DRAWER_OPEN,
                  ProjectConstants.STR_DRAWER_CLOSE
                ).also {
                  drawerToggle.set(it)
                  it.syncState()
                  getDrawerLayout().addDrawerListener(it)
                }
              }
            }
          }
        }
      }
    }
  }

  override fun onPause() {
    super.onPause()
    if (activity is DrawerActivity && sidebarEnabled) {
      drawerToggle.get()?.let {
        (activity as DrawerActivity).getDrawerLayout()
          .removeDrawerListener(it)
      }
      drawerToggle.set(null)
    }
  }
}
