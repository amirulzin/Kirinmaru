package commons.android.core.databinding

import android.annotation.TargetApi
import android.databinding.BindingAdapter
import android.os.Build
import android.support.annotation.ColorInt
import android.widget.Button

/**
 * Helper adapters for simple layout attributes
 */
object BindingAdapters {

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @JvmStatic
  @BindingAdapter("compoundTint")
  fun tintCompoundDrawables(button: Button, @ColorInt tintColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
      for (drawable in button.compoundDrawables) {
        drawable?.setTint(tintColor)
      }
  }
}