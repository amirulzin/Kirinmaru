package stream.reconfig.kirinmaru.plugins.novelfull

import okhttp3.HttpUrl
import stream.reconfig.kirinmaru.core.LinkTransformer
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail
import stream.reconfig.kirinmaru.core.parser.AbsChapterDetailParser
import stream.reconfig.kirinmaru.core.parser.AbsChapterIdParser
import stream.reconfig.kirinmaru.core.parser.AbsIndexParser

object NovelFullSearchParser : AbsIndexParser(
  selector = ".truyen-title a",
  transformer = { element ->
    CoreNovelDetail(
      origin = NOVELFULL_ORIGIN,
      novelTitle = element.ownText(),
      url = element.attr("href"))
  }
)

object NovelFullChapterIdParser : AbsChapterIdParser(
  chapterIds = ".list-chapter a",
  transformer = { element ->
    CoreChapterId(
      url = element.attr("href"),
      title = element.attr("title"))
  }
)

object NovelFullChapterDetailParser : AbsChapterDetailParser(
  title = ".truyen-title",
  rawText = "#chapter-content",
  prevUrl = "#prev_chap",
  nextUrl = "#next_chap"
)

object NovelFullLinkTransformer : LinkTransformer {
  override val baseUrl by lazy { HttpUrl.parse(NOVELFULL_HOME)!! }
}