package commons.android.core.keyboard

import android.app.Activity
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard() {
  val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  val token = (currentFocus ?: View(this)).windowToken
  imm.hideSoftInputFromWindow(token, 0)
}

fun Fragment.hideKeyboard() {
  val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
  //Find the currently focused view, so we can grab the correct window token from it.
  //If no view currently has focus, create a new one, just so we can grab a window token from it
  view?.rootView?.windowToken.let {
    imm?.hideSoftInputFromWindow(it, 0)
  }
}