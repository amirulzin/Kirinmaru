package commons.android.core.recycler

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View

class MarginItemDecoration(
  context: Context,
  @DimenRes horizontalMargin: Int,
  @DimenRes verticalMargin: Int
) : RecyclerView.ItemDecoration() {
  private var verticalMarginPixel: Int = 0
  private var horizontalMarginPixel: Int = 0

  init {
    verticalMarginPixel = context.resources.getDimensionPixelSize(verticalMargin)
    horizontalMarginPixel = context.resources.getDimensionPixelSize(horizontalMargin)
  }

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    if (parent.getChildAdapterPosition(view) == 0) outRect.top = verticalMarginPixel * 2 //double margin at top
    outRect.bottom = verticalMarginPixel
    outRect.left = horizontalMarginPixel
    outRect.right = horizontalMarginPixel
  }
}

class HorizontalMarginItemDecoration(
  context: Context,
  @DimenRes horizontalMargin: Int,
  @DimenRes verticalMargin: Int
) : RecyclerView.ItemDecoration() {
  private var verticalMarginPixel: Int = 0
  private var horizontalMarginPixel: Int = 0

  init {
    verticalMarginPixel = context.resources.getDimensionPixelSize(verticalMargin)
    horizontalMarginPixel = context.resources.getDimensionPixelSize(horizontalMargin)
  }

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    outRect.bottom = verticalMarginPixel
    outRect.top = verticalMarginPixel
    if (parent.getChildAdapterPosition(view) == 0) outRect.left = verticalMarginPixel * 2
    outRect.right = horizontalMarginPixel
  }
}