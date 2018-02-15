package com.reconfig.kirinmaru.plugins.gravitytales

import com.reconfig.kirinmaru.TestHelper
import okhttp3.CookieJar
import org.junit.Assert.assertTrue
import org.junit.Test
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelId
import stream.reconfig.kirinmaru.plugins.gravitytales.GravityTalesPlugin

/**
 * Test for GravityTales Plugin
 */
class GravityTalesPluginTest {
  val plugin = GravityTalesPlugin(TestHelper.okHttpClient(), CookieJar.NO_COOKIES)

  @Test
  fun obtainNovels() {
    plugin.obtainNovels()
        .map {
          assertTrue(it.isNotEmpty())
          it.onEach {
            assertTrue(it.url.isNotBlank())
            assertTrue(it.id!!.isNotBlank())
          }
        }.test().assertNoErrors().assertComplete()
  }

  @Test
  fun obtainChapters() {
    plugin.obtainChapters(CoreNovelId("Chaotic Sword God", "chaotic-sword-god", "5")) //as returned by obtainNovels()
        .map {
          assertTrue(it.isNotEmpty())
          it.onEach {
            assertTrue(it.url.isNotBlank())
          }
        }.test().assertNoErrors().assertComplete()
  }

  @Test
  fun obtainDetail() {
    plugin.obtainDetail(CoreChapterId("novel/chaotic-sword-god/csg-chapter-1081"))
        .map {
          println(it)
          with(it) {
            assertTrue(rawText!!.isNotBlank())
            assertTrue(nextUrl!!.isNotBlank())
            assertTrue(previousUrl!!.isNotBlank())
          }
        }.test().assertNoErrors().assertComplete()
  }
}