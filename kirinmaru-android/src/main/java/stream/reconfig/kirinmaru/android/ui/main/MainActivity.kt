package stream.reconfig.kirinmaru.android.ui.main

import android.os.Bundle
import android.support.annotation.IdRes
import android.view.Menu
import android.view.MenuItem
import stream.reconfig.kirinmaru.android.ui.common.activity.DrawerBindingActivity
import stream.reconfig.kirinmaru.android.ui.navigation.FragmentNavigator
import stream.reconfig.kirinmaru.android.util.livedata.observeNonNull
import stream.reconfig.kirinmaru.android.util.viewmodel.ViewModelFactory
import stream.reconfig.kirinmaru.android.util.viewmodel.viewModel
import javax.inject.Inject

class MainActivity : DrawerBindingActivity() {

  @Inject
  lateinit var vmf: ViewModelFactory

  private val mvm by lazy { viewModel(vmf, MainViewModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    bindContentView()
    binding.drawerNavigation.setNavigationItemSelectedListener(this)

    mvm.appState.observeNonNull(this) { appState ->
      appState.apply {
        if (isFirstLoad) {
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
        mvm.appState.value!!.firstOrigin
    )
  }

  private fun initDrawerNav(@IdRes id: Int) {
    binding.drawerNavigation.apply {
      menu.performIdentifierAction(id, Menu.FLAG_PERFORM_NO_CLOSE)
      setCheckedItem(id)
    }
  }
}