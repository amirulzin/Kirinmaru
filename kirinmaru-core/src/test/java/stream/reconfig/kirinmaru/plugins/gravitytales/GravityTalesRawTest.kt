package stream.reconfig.kirinmaru.plugins.gravitytales

import org.jsoup.Jsoup
import org.junit.Test
import stream.reconfig.kirinmaru.TestHelper
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail

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
    val novelId = CoreNovelDetail(GRAVITYTALES_ORIGIN, "Chaotic Sword God", "chaotic-sword-god")
    val result = GravityTalesChapterDetailParser(novelId)
        .parse(Jsoup.parse(html))
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