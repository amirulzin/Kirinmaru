package stream.reconfig.kirinmaru.plugins.wuxiaworld

import okhttp3.CookieJar
import org.junit.Test
import stream.reconfig.kirinmaru.TestHelper
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail
import stream.reconfig.kirinmaru.plugins.PluginTestHelper

class WuxiaworldPluginTest {

  val plugin = WuxiaworldPlugin(TestHelper.okHttpClient(), CookieJar.NO_COOKIES)

  val testNovel = CoreNovelDetail(
      novelTitle = "I Shall Seal the Heavens",
      url = "/novel/i-shall-seal-the-heavens",
      id = null,
      tags = setOf("Chinese", "Completed"),
      origin = WUXIAWORLD_ORIGIN
  )

  val testChapter = CoreChapterId("/novel/i-shall-seal-the-heavens/issth-book-9-chapter-1453", "Chapter 1453: Enjoy Yourself, Prince!")

  @Test
  fun obtainNovels() {
    PluginTestHelper(plugin).verifyObtainNovels()
  }

  @Test
  fun obtainChapters() {
    PluginTestHelper(plugin).verifyObtainChapterIds(testNovel)
  }

  @Test
  fun obtainDetail() {
    PluginTestHelper(plugin).verifyObtainChapterDetail(testNovel, testChapter)
  }

  @Test
  fun toAbsoluteUrl() {
    PluginTestHelper(plugin).verifyAbsoluteUrl(testNovel, testChapter)
  }

  @Test
  fun testIntegration() {
    PluginTestHelper(plugin).verifyIntegration()
  }
}