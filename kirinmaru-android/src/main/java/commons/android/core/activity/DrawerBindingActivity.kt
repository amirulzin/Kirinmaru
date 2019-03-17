package commons.android.core.activity

import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import commons.android.DrawerActivityBindingAlias
import commons.android.ProjectConstants

/**
 * Lightly implemented Drawer activity based on [DataBindingActivity]
 */
abstract class DrawerBindingActivity : DataBindingActivity<DrawerActivityBindingAlias>(), NavigationView.OnNavigationItemSelectedListener, DrawerActivity {
  override val layoutId: Int = ProjectConstants.LYT_BASE_DRAWER

  override val contentFrameId: Int = ProjectConstants.LYT_BASE_DRAWER_CONTENT_FRAME

  override fun getDrawerLayout() = binding.drawerLayout

  override fun bindNavigationListener() = binding.drawerNavigation.setNavigationItemSelectedListener(this)

  override fun initDrawerNav(@IdRes id: Int, commit: Boolean) {
    binding.drawerNavigation.apply {
      if (commit)
        menu.performIdentifierAction(id, Menu.FLAG_PERFORM_NO_CLOSE)

      setCheckedItem(id)
    }
  }

  override fun toggleSidebar(enabled: Boolean) {
    getDrawerLayout().setDrawerLockMode(if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
  }
}

