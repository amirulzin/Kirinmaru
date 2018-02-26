package com.reconfig.kirinmaru.plugins.gravitytales

import com.reconfig.kirinmaru.TestHelper
import okhttp3.CookieJar
import org.junit.Assert.assertTrue
import org.junit.Test
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelId
import stream.reconfig.kirinmaru.plugins.gravitytales.GRAVITYTALES_HOME
import stream.reconfig.kirinmaru.plugins.gravitytales.GravityTalesPlugin

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
          }
        }.test().assertNoErrors().assertComplete()
  }

  @Test
  fun obtainChapters() {

    plugin.obtainChapters(novel) //as returned by obtainNovels()
        .map {
          assertTrue(it.isNotEmpty())
          it.onEach {
            assertTrue(it.url.isNotBlank())
          }
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
    plugin.toAbsoluteUrl(novel, chapterId)
        .map {
          val matcher = GRAVITYTALES_HOME + chapterId.url
          assertTrue("absoluteUrl doesn't match. \nFound: $it\nMatcher:$matcher", it == matcher)
        }.test().assertNoErrors().assertComplete()
  }
}