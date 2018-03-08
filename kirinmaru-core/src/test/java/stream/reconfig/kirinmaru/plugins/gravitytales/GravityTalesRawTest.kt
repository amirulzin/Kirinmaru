package stream.reconfig.kirinmaru.plugins.gravitytales

import stream.reconfig.kirinmaru.TestHelper
import org.jsoup.Jsoup
import org.junit.Test
import stream.reconfig.kirinmaru.plugins.gravitytales.GravityTalesChapterDetailParser
import stream.reconfig.kirinmaru.plugins.gravitytales.GravityTalesIndexParser
import stream.reconfig.kirinmaru.plugins.gravitytales.GravityTalesLinkTransformer

/**
 * Local test for GravityTales parser
 */
class GravityTalesRawTest {

  @Test
  fun testParseChapter() {
    /**
     * http://gravitytales.com/Novel/chaotic-sword-god/csg-chapter-619
     */

    val html = TestHelper.readResource("gravity-tales-csg-619.html")
    val result = GravityTalesChapterDetailParser.parse(Jsoup.parse(html))
    println(result.nextUrl)
    println(result.previousUrl)
    println(result.rawText)
  }

  @Test
  fun testIndexParser() {
    val html = TestHelper.readResource("gravity-tales-home.html")
    val list = GravityTalesIndexParser.parse(Jsoup.parse(html))
    list.forEach { println("${it.novelTitle} : [${GravityTalesLinkTransformer.toAbsolute(it.url)}]") }
  }

  @Test
  fun testChapterIdParser() {
    val html = TestHelper.readResource("gravity-tales-home.html")
    val list = GravityTalesIndexParser.parse(Jsoup.parse(html))
    list.forEach { println("${it.novelTitle} : [${GravityTalesLinkTransformer.toAbsolute(it.url)}]") }
  }
}