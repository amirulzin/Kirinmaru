package stream.reconfig.kirinmaru.android.util.recycler

import android.content.res.Resources
import commons.android.core.recycler.VerticalItemDecoration
import stream.reconfig.kirinmaru.android.R

/**
 *
 */
object ItemDecorationUtil {
  @JvmStatic
  fun defaultVerticalDecor(resources: Resources) = VerticalItemDecoration(resources.getDimensionPixelSize(R.dimen.recycler_item_margin))
}