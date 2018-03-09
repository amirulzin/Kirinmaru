package stream.reconfig.kirinmaru.android.ui.common.activity

import android.support.design.widget.NavigationView
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.ActivityBaseDrawerBinding

/**
 * Lightly implemented Drawer activity based on [DataBindingActivity]
 */
abstract class DrawerBindingActivity : DataBindingActivity<ActivityBaseDrawerBinding>(), NavigationView.OnNavigationItemSelectedListener {
  override val layoutId: Int = R.layout.activity_base_drawer

  fun getDrawerLayout() = binding.drawerLayout
}