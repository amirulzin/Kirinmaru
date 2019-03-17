package commons.android.core.permission

import android.content.pm.PackageManager
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

inline fun Fragment.checkPermission(requestCode: Int, permission: String, crossinline onAlreadyGranted: () -> Unit) {
  activity?.let { act ->
    if (ContextCompat.checkSelfPermission(act, permission) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(arrayOf(permission), requestCode)
    } else {
      onAlreadyGranted()
    }
  }
}