package stream.reconfig.kirinmaru.plugins.gravitytales

import okhttp3.CookieJar
import org.junit.Assert.assertTrue
import org.junit.Test
import stream.reconfig.kirinmaru.TestHelper
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail
import stream.reconfig.kirinmaru.plugins.PluginTestHelper

/**
 * Test for GravityTales Plugin
 */
class GravityTalesPluginTest {

  val plugin = GravityTalesPlugin(TestHelper.okHttpClient(), CookieJar.NO_COOKIES)

  val novel = CoreNovelDetail(GRAVITYTALES_ORIGIN, "Chaotic Sword God", "chaotic-sword-god", "5")

  val chapterId = CoreChapterId("csg-chapter-1081", "Chapter 1081: The Bell of Grand Clarity Chimes Nine Times")

  @Test
  fun obtainNovels() {
    PluginTestHelper(plugin).verifyObtainNovels { list ->
      assertTrue(list.isNotEmpty())
      list.forEach {
        with(it) {
          assertTrue(url.isNotBlank())
          assertTrue(id!!.isNotBlank())
          assertTrue(novelTitle.isNotBlank())
          assertTrue(tags.isEmpty())
        }
      }
    }
  }

  @Test
  fun obtainChapters() {
    PluginTestHelper(plugin).verifyObtainChapterIds(novel)
  }

  @Test
  fun obtainDetail() {
    PluginTestHelper(plugin).verifyObtainChapterDetail(novel, chapterId)
  }

  @Test
  fun toAbsolute() {
    PluginTestHelper(plugin).verifyAbsoluteUrl(novel, chapterId)
  }

  @Test
  fun testIntegration() {
    PluginTestHelper(plugin).verifyIntegration()
  }
}