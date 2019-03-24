package stream.reconfig.kirinmaru.plugins.wuxiaworld

import okhttp3.HttpUrl
import org.jsoup.nodes.Document
import stream.reconfig.kirinmaru.core.ChapterDetail
import stream.reconfig.kirinmaru.core.LinkTransformer
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail
import stream.reconfig.kirinmaru.core.parser.AbsChapterDetailParser
import stream.reconfig.kirinmaru.core.parser.AbsChapterIdParser
import stream.reconfig.kirinmaru.core.parser.AbsIndexParser
import stream.reconfig.kirinmaru.core.selectBy

internal class WuxiaWorldIndexParserV2(val key: String) : AbsIndexParser(
  selector = ".media-heading a[href*=/novel/]",
  transformer = { CoreNovelDetail(WUXIAWORLD_ORIGIN, it.text(), it.attr("href"), null, mutableSetOf(key)) }
)


internal object WuxiaWorldChapterIdParser : AbsChapterIdParser(
  chapterIds = ".section-content .chapter-item a[href*=/novel/]",
  transformer = { CoreChapterId(it.attr("href"), it.text()) }
)

internal object WuxiaWorldChapterDetailParser : AbsChapterDetailParser(
  title = "meta[property=og:title]",
  rawText = ".fr-view",
  nextUrl = ".next a[href*=/novel/]",
  prevUrl = ".prev a[href*=/novel/]"
) {
  override fun parse(document: Document): ChapterDetail {
    val result = transformer(
      document.selectBy(title) { first()?.attr("content") },
      document.selectBy(rawText) { html() },
      document.selectBy(nextUrl) { first()?.attr("href") },
      document.selectBy(prevUrl) { first()?.attr("href") }
    )
    return clean?.invoke(result) ?: result
  }
}

internal object WuxiaWorldLinkTransformer : LinkTransformer {
  override val baseUrl = HttpUrl.parse(WUXIAWORLD_HOME)!!
}