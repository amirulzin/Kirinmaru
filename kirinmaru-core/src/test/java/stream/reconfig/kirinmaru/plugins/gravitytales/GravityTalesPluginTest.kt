package stream.reconfig.kirinmaru.plugins.gravitytales

import okhttp3.CookieJar
import org.junit.Assert.assertTrue
import org.junit.Test
import stream.reconfig.kirinmaru.TestHelper
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelId

/**
 * Test for GravityTales Plugin
 */
class GravityTalesPluginTest {
  val plugin = GravityTalesPlugin(TestHelper.okHttpClient(), CookieJar.NO_COOKIES)
  val novel = CoreNovelId("Chaotic Sword God", "chaotic-sword-god", "5")
  val chapterId = CoreChapterId("novel/chaotic-sword-god/csg-chapter-1081")

  @Test
  fun obtainNovels() {
    plugin.obtainNovels()
        .map { list ->
          assertTrue(list.isNotEmpty())
          list.onEach {
            with(it) {
              assertTrue(url.isNotBlank())
              assertTrue(id!!.isNotBlank())
              assertTrue(novelTitle.isNotBlank())
              assertTrue(tags.isEmpty())
            }
          }.first().let(::println)
        }.test().assertNoErrors().assertComplete()
  }

  @Test
  fun obtainChapters() {

    plugin.obtainChapters(novel) //as returned by obtainNovels()
        .map {
          assertTrue(it.isNotEmpty())
          it.onEach {
            assertTrue(it.url.isNotBlank())
          }.first().let(::println)
        }.test().assertNoErrors().assertComplete()
  }

  @Test
  fun obtainDetail() {
    plugin.obtainDetail(chapterId)
        .map {
          println(it)
          with(it) {
            assertTrue(rawText!!.isNotBlank())
            assertTrue(nextUrl!!.isNotBlank())
            assertTrue(previousUrl!!.isNotBlank())
          }
        }.test().assertNoErrors().assertComplete()
  }

  @Test
  fun toAbsolute() {
    val matcher = GRAVITYTALES_HOME + chapterId.url
    val str = plugin.toAbsoluteUrl(novel, chapterId)
    assertTrue("absoluteUrl doesn't match. \nFound: $str\nMatcher:$matcher", str == matcher)
  }
}