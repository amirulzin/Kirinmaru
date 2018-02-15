package stream.reconfig.kirinmaru.core.parser

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.selectBy

/**
 *
 */
abstract class AbsChapterIdParser(
    val chapterIds: String,
    inline val transformer: (Element) -> ChapterId
) : Parser<List<ChapterId>> {
  override fun parse(document: Document): List<ChapterId> {
    return document.selectBy(chapterIds) {
      it.map(transformer).filter { it.url.isNotBlank() }
    } ?: emptyList<ChapterId>()
  }
}