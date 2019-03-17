package commons.android.core.tabs

import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.UiThread
import android.support.design.widget.TabLayout
import android.support.v4.graphics.drawable.DrawableCompat

@UiThread
fun TabLayout.tintIcon(@ColorRes colorRes: Int) {

  val colors = if (Build.VERSION.SDK_INT >= 23) {
    resources.getColorStateList(colorRes, context.theme)
  } else {
    resources.getColorStateList(colorRes)
  }

  for (i in 0 until tabCount) {
    getTabAt(i)?.icon?.let {
      DrawableCompat.wrap(it)
      DrawableCompat.setTintList(it, colors)
    }
  }
}
