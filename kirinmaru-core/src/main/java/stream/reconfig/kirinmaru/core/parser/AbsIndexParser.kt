package stream.reconfig.kirinmaru.core.parser

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import stream.reconfig.kirinmaru.core.NovelId
import stream.reconfig.kirinmaru.core.selectBy

/**
 *
 */
abstract class AbsIndexParser(
    open val selector: String,
    open inline val transformer: (Element) -> NovelId
) : Parser<List<NovelId>> {

  override fun parse(document: Document): List<NovelId> {
    return document.selectBy(selector) {
      map(transformer).filter { it.novelTitle.isNotBlank() && it.url.isNotBlank() }
    } ?: emptyList()
  }
}