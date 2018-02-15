package stream.reconfig.kirinmaru.core.parser

import org.jsoup.nodes.Document

/**
 * Default Parser interface for any type of Jsoup parser
 */
interface Parser<out T> {
  fun parse(document: Document): T
}