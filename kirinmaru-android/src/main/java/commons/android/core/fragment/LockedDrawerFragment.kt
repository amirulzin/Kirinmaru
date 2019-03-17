package commons.android.core.fragment

import android.databinding.ViewDataBinding

abstract class LockedDrawerFragment<V : ViewDataBinding> : DrawerFragment<V>() {
  override val sidebarEnabled = false
  override val bottomNavEnabled = false
}