package stream.reconfig.kirinmaru.android.util.recycler

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import stream.reconfig.kirinmaru.android.util.context.ContextUtils

class VerticalItemDecoration : RecyclerView.ItemDecoration {
  private var pixelMargin: Int = 0

  constructor(pixelMargin: Int) {
    this.pixelMargin = pixelMargin
  }

  constructor(context: Context, dpMargin: Float) {
    this.pixelMargin = ContextUtils.getPixels(context, dpMargin)
  }

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

    if (parent.getChildAdapterPosition(view) == 0) outRect.top = pixelMargin

    outRect.bottom = pixelMargin
    outRect.left = pixelMargin
    outRect.right = pixelMargin

  }
}