package commons.android.core.permission

import android.support.v4.app.Fragment

abstract class PermissionHandler(
  private val fragment: Fragment,
  private val requestCode: Int,
  private val permission: String
) {

  abstract fun onGranted()

  fun handleRequest(bypass: Boolean) {
    if (bypass) onGranted()
    else {
      fragment.checkPermission(requestCode, permission, ::onGranted)
    }
  }
}