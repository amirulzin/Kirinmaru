package stream.reconfig.kirinmaru.plugins.gravitytales

import okhttp3.HttpUrl
import stream.reconfig.kirinmaru.core.LinkTransformer
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelId
import stream.reconfig.kirinmaru.core.parser.AbsChapterDetailParser
import stream.reconfig.kirinmaru.core.parser.AbsChapterIdParser
import stream.reconfig.kirinmaru.core.parser.AbsIndexParser

internal object GravityTalesIndexParser : AbsIndexParser(
    selector = ".multi-column-dropdown a[href*=/novel/]",
    transformer = { CoreNovelId(it.text(), it.attr("href")) }
)

internal object GravityTalesChapterIdParser : AbsChapterIdParser(
    chapterIds = "#chapters a[href*=/novel/]",
    transformer = { CoreChapterId(it.attr("href")) }
)

internal object GravityTalesChapterDetailParser : AbsChapterDetailParser(
    rawText = "#chapterContent",
    nextUrl = ".chapter-navigation a:contains(next chapter)",
    prevUrl = ".chapter-navigation a:contains(previous chapter)"
)

internal object GravityTalesLinkTransformer : LinkTransformer {
  override val baseUrl = HttpUrl.parse(GRAVITYTALES_HOME)!!
}


