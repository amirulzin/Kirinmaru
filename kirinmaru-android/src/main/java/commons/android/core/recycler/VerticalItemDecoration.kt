package commons.android.core.recycler

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View
import commons.android.core.context.ContextUtils

class VerticalItemDecoration : RecyclerView.ItemDecoration {
  private var pixelMargin: Int = 0

  constructor(pixelMargin: Int) {
    this.pixelMargin = pixelMargin
  }

  constructor(context: Context, dpMargin: Float) {
    this.pixelMargin = ContextUtils.getPixels(context, dpMargin)
  }

  constructor(context: Context, @DimenRes dimenRes: Int) {
    this.pixelMargin = context.resources.getDimensionPixelSize(dimenRes)
  }

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    if (parent.getChildAdapterPosition(view) == 0) outRect.top = pixelMargin
    outRect.bottom = pixelMargin
    outRect.left = pixelMargin
    outRect.right = pixelMargin
  }
}