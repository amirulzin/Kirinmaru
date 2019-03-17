package commons.android.core.activity

import android.support.annotation.IdRes
import android.support.v4.widget.DrawerLayout
import android.view.Gravity

interface DrawerActivity {
  val contentFrameId: Int
  fun getDrawerLayout(): DrawerLayout
  fun bindNavigationListener()
  fun initDrawerNav(@IdRes id: Int, commit: Boolean)
  fun toggleSidebar(enabled: Boolean)
  fun toggleBottomNav(enabled: Boolean)
  fun showDrawer(gravity: Int = Gravity.START)
}