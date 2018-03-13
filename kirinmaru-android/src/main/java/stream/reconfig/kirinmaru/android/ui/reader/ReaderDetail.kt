package stream.reconfig.kirinmaru.android.ui.reader

import android.text.Spanned


data class ReaderDetail(
    val text: Spanned? = null,
    val url: String,
    val previousUrl: String? = null,
    val nextUrl: String? = null
) {
  fun canNavigateNext() = !nextUrl.isNullOrBlank()

  fun canNavigatePrevious() = !previousUrl.isNullOrBlank()

  fun hasText() = !text.isNullOrBlank()
}


