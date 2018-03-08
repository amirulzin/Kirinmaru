package stream.reconfig.kirinmaru.plugins.wuxiaworld

import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelId
import stream.reconfig.kirinmaru.core.parser.AbsChapterDetailParser
import stream.reconfig.kirinmaru.core.parser.AbsChapterIdParser
import stream.reconfig.kirinmaru.core.parser.AbsIndexParser

internal class WuxiaWorldIndexParserV2(val key: String) : AbsIndexParser(
    selector = ".media-heading a[href*=/novel/]",
    transformer = { CoreNovelId(it.text(), it.attr("href"), null, mutableSetOf(key)) }
)


internal object WuxiaWorldChapterIdParser : AbsChapterIdParser(
    chapterIds = ".section-content .chapter-item a[href*=/novel/]",
    transformer = { CoreChapterId(it.attr("href")) }
)

internal object WuxiaWorldChapterDetailParser : AbsChapterDetailParser(
    rawText = ".fr-view",
    nextUrl = ".next a",
    prevUrl = ".prev a"
)