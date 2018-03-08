package stream.reconfig.kirinmaru.core.parser

import org.jsoup.nodes.Document
import stream.reconfig.kirinmaru.core.ChapterDetail
import stream.reconfig.kirinmaru.core.domain.CoreChapterDetail
import stream.reconfig.kirinmaru.core.selectBy

abstract class AbsChapterDetailParser(
    open val rawText: String,
    open val nextUrl: String,
    open val prevUrl: String,
    open inline val transformer: (rawText: String?, nextUrl: String?, prevUrl: String?) -> ChapterDetail = ::CoreChapterDetail
) : Parser<ChapterDetail> {

  override fun parse(document: Document): ChapterDetail {
    return transformer(
        document.selectBy(rawText) { it.html() },
        document.selectBy(nextUrl) { it.first().attr("href") },
        document.selectBy(prevUrl) { it.first().attr("href") }
    )
  }
}

