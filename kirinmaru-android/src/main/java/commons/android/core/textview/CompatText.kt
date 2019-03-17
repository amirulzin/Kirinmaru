package commons.android.core.textview

import android.os.Build
import android.text.Html
import android.text.Spanned

object CompatText {

  @JvmStatic
  fun toHtmlSpannable(string: String): Spanned {
    @Suppress("DEPRECATION")
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
      Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
    else Html.fromHtml(string)
  }

}