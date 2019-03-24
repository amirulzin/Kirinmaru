package stream.reconfig.kirinmaru.plugins.novelfull

import okhttp3.CookieJar
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import stream.reconfig.kirinmaru.TestHelper
import stream.reconfig.kirinmaru.core.SearchOptionsBuilder
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail
import stream.reconfig.kirinmaru.core.setTerm
import stream.reconfig.kirinmaru.plugins.PluginTestHelper

class NovelFullPluginTest {

  val plugin = NovelFullPlugin(TestHelper.okHttpClient(HttpLoggingInterceptor.Level.HEADERS), CookieJar.NO_COOKIES)

  val testNovel = CoreNovelDetail(
    novelTitle = "Seeking the Flying Sword Path",
    url = "/seeking-the-flying-sword-path.html",
    id = null,
    tags = setOf(),
    origin = NOVELFULL_ORIGIN
  )

  val testChapter = CoreChapterId("/seeking-the-flying-sword-path/chapter-46.html", "Chapter 46")

  private val helper = PluginTestHelper(plugin, logging = true)

  @Test
  fun obtainPreliminaryNovels() {
    helper.verifyObtainPreliminaryNovels()
  }

  @Test
  fun obtainNovels() {
    helper.verifyObtainNovels(SearchOptionsBuilder.new().setTerm("a"))
  }

  @Test
  fun obtainChapters() {
    helper.verifyObtainChapterIds(testNovel)
  }

  @Test
  fun obtainDetail() {
    helper.verifyObtainChapterDetail(testNovel, testChapter)
  }

  @Test
  fun toAbsoluteUrl() {
    helper.verifyAbsoluteUrl(testNovel, testChapter)
  }

  @Test
  fun testIntegration() {
    helper.verifyIntegration()
  }
}