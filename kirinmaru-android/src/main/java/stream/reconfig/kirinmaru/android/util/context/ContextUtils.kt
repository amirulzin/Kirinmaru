package stream.reconfig.kirinmaru.android.util.context

import android.content.Context
import android.util.TypedValue

object ContextUtils {

  /**
   * Get pixels from the given dp (rounded via {@link Math#round})
   */
  @JvmStatic
  fun getPixels(context: Context, dp: Float): Int {
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics))
  }

  /**
   * Get dp from the given pixel (rounded via {@link Math#round})
   */
  @JvmStatic
  fun getDp(context: Context, px: Int): Int {
    return Math.round(px / context.resources.displayMetrics.density)
  }
}