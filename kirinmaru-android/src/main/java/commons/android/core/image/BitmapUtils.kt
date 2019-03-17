package commons.android.core.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.support.annotation.WorkerThread
import java.io.IOException
import java.io.InputStream

object BitmapUtils {

  private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
      val halfHeight: Int = height / 2
      val halfWidth: Int = width / 2
      while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
        inSampleSize *= 2
      }
    }

    return inSampleSize
  }

  @WorkerThread
  @JvmStatic
  fun decodeInputStream(inputStream: InputStream, reqWidth: Int, reqHeight: Int): Bitmap {
    return BitmapFactory.Options().run {
      inJustDecodeBounds = true
      BitmapFactory.decodeStream(inputStream) ?: throw IOException("Can't decode stream")
      inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
      inJustDecodeBounds = false
      BitmapFactory.decodeStream(inputStream, Rect(0, 0, 0, 0), this)
        ?: throw IOException("Can't decode resampled stream")
    }
  }
}