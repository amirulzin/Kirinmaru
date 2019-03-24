package stream.reconfig.kirinmaru.core.parser

import org.jsoup.nodes.Document
import stream.reconfig.kirinmaru.core.ChapterDetail
import stream.reconfig.kirinmaru.core.domain.CoreChapterDetail
import stream.reconfig.kirinmaru.core.selectBy

abstract class AbsChapterDetailParser(
  open val title: String,
  open val rawText: String,
  open val nextUrl: String,
  open val prevUrl: String,
  open inline val transformer: (title: String?, rawText: String?, nextUrl: String?, prevUrl: String?) -> ChapterDetail = ::CoreChapterDetail,
  open inline val clean: ((ChapterDetail) -> ChapterDetail)? = null
) : Parser<ChapterDetail> {

  override fun parse(document: Document): ChapterDetail {
    val result = transformer(
      document.selectBy(title) { text() },
      document.selectBy(rawText) { html() },
      document.selectBy(nextUrl) { first()?.attr("href") },
      document.selectBy(prevUrl) { first()?.attr("href") }
    )
    return clean?.invoke(result) ?: result
  }
}

