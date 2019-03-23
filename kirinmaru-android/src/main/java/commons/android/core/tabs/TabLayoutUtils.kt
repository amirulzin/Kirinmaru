package commons.android.core.tabs

import android.support.annotation.ColorRes
import android.support.annotation.UiThread
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat

@UiThread
fun TabLayout.tintIcon(@ColorRes colorRes: Int) {
  ContextCompat.getColorStateList(context, colorRes)?.let { colors ->
    for (i in 0 until tabCount) {
      getTabAt(i)?.icon?.let { drawable ->
        DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(drawable, colors)
      }
    }
  }
}
