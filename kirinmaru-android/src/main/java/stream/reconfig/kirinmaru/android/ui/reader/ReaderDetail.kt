package stream.reconfig.kirinmaru.android.ui.reader

import android.text.Spanned


data class ReaderDetail(
    val text: Spanned? = null,
    val url: String,
    val previousUrl: String? = null,
    val nextUrl: String? = null,
    val taxon: String
) {
  fun canNavigateNext() = !nextUrl.isNullOrBlank()

  fun canNavigatePrevious() = !previousUrl.isNullOrBlank()

  fun hasText() = !text.isNullOrBlank()
}


