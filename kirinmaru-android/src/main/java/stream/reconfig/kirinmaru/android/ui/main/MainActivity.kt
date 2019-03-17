package stream.reconfig.kirinmaru.android.ui.main

import android.os.Bundle
import android.support.annotation.IdRes
import android.view.Menu
import android.view.MenuItem
import commons.android.arch.ViewModelFactory
import commons.android.arch.observeNonNull
import commons.android.arch.viewModel
import commons.android.core.activity.DrawerBindingActivity
import stream.reconfig.kirinmaru.android.ui.navigation.FragmentNavigator
import javax.inject.Inject

class MainActivity : DrawerBindingActivity() {
  override fun toggleBottomNav(enabled: Boolean) {

  }

  override fun showDrawer(gravity: Int) {

  }

  @Inject
  lateinit var vmf: ViewModelFactory

  private val mvm by lazy { viewModel(vmf, MainViewModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    bindContentView()
    binding.drawerNavigation.setNavigationItemSelectedListener(this)

    mvm.appState.observeNonNull(this) { appState ->
      appState.apply {
        if (isFirstLoad == true) {
          //TODO future firstLoad features
        }
        if (savedInstanceState == null) {
          initDrawerNav(firstNav)
        }
      }
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    return FragmentNavigator.sideNavigate(
      this,
      item,
      binding.drawerNavigation.menu,
      mvm.appState.value!!.firstOrigin!!
    )
  }

  private fun initDrawerNav(@IdRes id: Int) {
    binding.drawerNavigation.apply {
      menu.performIdentifierAction(id, Menu.FLAG_PERFORM_NO_CLOSE)
      setCheckedItem(id)
    }
  }
}