package stream.reconfig.kirinmaru.plugins.novelfull

import okhttp3.HttpUrl
import org.jsoup.nodes.Document
import stream.reconfig.kirinmaru.core.LinkTransformer
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail
import stream.reconfig.kirinmaru.core.parser.*

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

object NovelFullAllChapterIdParser : AbsChapterIdParser(
  chapterIds = "option",
  transformer = { element ->
    CoreChapterId(
      url = element.attr("value"),
      title = element.ownText())
  }
)

object NovelFullNovelIdParser : Parser<Long> {
  private const val searchKeyMarker = "var novel = {"
  private const val searchKeyEnd = ","
  private const val searchKeyStart = ":"
  private const val WRONG_ID = -1L
  private val TAG = javaClass.simpleName

  @JvmStatic
  fun validateId(id: Long) = id.apply {
    if (this == WRONG_ID) throw ParseException("Broken novelId")
  }

  override fun parse(document: Document): Long {
    for (element in document.getElementsByTag("script")) {
      val dataNodes = element.dataNodes()
      if (dataNodes.isNotEmpty()) {
        dataNodes.forEach {
          val data = it.wholeData
          val markerBegin = data.indexOf(searchKeyMarker)
          if (markerBegin != -1) {

            val valueBegin = data.indexOf(searchKeyStart, markerBegin)
            if (valueBegin == -1) throw ParseException("$TAG: Invalid valueBegin")

            val valueEnd = data.indexOf(searchKeyEnd, markerBegin)
            if (valueEnd == -1) throw ParseException("$TAG: Invalid valueEnd")

            val id = data.substring(valueBegin + searchKeyStart.length, valueEnd).trim()
            return id.toLongOrNull() ?: WRONG_ID
          }

        }
      }
    }
    return WRONG_ID
  }
}

object NovelFullChapterDetailParser : AbsChapterDetailParser(
  title = ".truyen-title",
  rawText = "#chapter-content",
  prevUrl = "#prev_chap",
  nextUrl = "#next_chap"
)

object NovelFullLinkTransformer : LinkTransformer {
  override val baseUrl by lazy { HttpUrl.parse(NOVELFULL_HOME)!! }
}