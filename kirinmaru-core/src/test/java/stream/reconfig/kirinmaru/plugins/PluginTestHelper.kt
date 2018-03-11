package stream.reconfig.kirinmaru.plugins

import okhttp3.HttpUrl
import org.junit.Assert.assertTrue
import stream.reconfig.kirinmaru.core.ChapterDetail
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.NovelId
import stream.reconfig.kirinmaru.core.Plugin

class PluginTestHelper(val plugin: Plugin, val logging: Boolean = false) {

  inline fun verifyObtainNovels(
      crossinline assertBlock: (List<NovelId>) -> Unit = {
        assertTrue("NovelIds is empty", it.isNotEmpty())
        if (logging) println(it.size)

        it.forEach {
          if (logging) println(it)
          with(it) {
            assertTrue("Title empty : $it", novelTitle.isNotBlank())
            assertTrue("Url empty : $it", url.isNotBlank())
          }
        }
      }
  ) {
    plugin.obtainNovels()
        .map { assertBlock(it) }
        .doOnError { it.printStackTrace() }
        .test().assertNoErrors().assertComplete()
  }

  inline fun verifyObtainChapterIds(
      novelId: NovelId,
      crossinline assertBlock: (List<ChapterId>) -> Unit = {
        assertTrue("Chapter urls is empty", it.isNotEmpty())
        if (logging) println(it.size)
        it.forEach {
          if (logging) println(it)
          assertTrue("Chapter url is blank: $novelId", it.url.isNotBlank())
        }
      }
  ) {
    plugin.obtainChapters(novelId)
        .map { assertBlock(it) }
        .test().assertComplete().assertNoErrors()
  }

  inline fun verifyObtainChapterDetail(
      chapter: ChapterId,
      crossinline assertBlock: (ChapterDetail) -> Unit = {
        if (logging) println(it)
        assertTrue("Next url is null or blank", !it.nextUrl.isNullOrBlank())
        assertTrue("Previous url is  null or blank", !it.previousUrl.isNullOrBlank())
        assertTrue("Raw text is null or blank", !it.rawText.isNullOrBlank())
      }
  ) {
    plugin.obtainDetail(chapter)
        .map { assertBlock(it) }
        .test()
        .assertNoErrors().assertComplete()
  }


  fun verifyAbsoluteUrl(
      novelId: NovelId,
      chapterId: ChapterId,
      assertBlock: (String) -> Unit = { assertTrue("Malformed url: $it", HttpUrl.parse(it) != null) }
  ) {
    val url = plugin.toAbsoluteUrl(novelId, chapterId)
    if (logging) println(url)
    assertBlock(url)
  }
}