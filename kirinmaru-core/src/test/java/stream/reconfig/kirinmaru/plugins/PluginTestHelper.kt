package stream.reconfig.kirinmaru.plugins

import okhttp3.HttpUrl
import okhttp3.Request
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import stream.reconfig.kirinmaru.TestHelper
import stream.reconfig.kirinmaru.core.ChapterDetail
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.NovelDetail
import stream.reconfig.kirinmaru.core.Plugin
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class PluginTestHelper(val plugin: Plugin, val logging: Boolean = false) {

  val logger = SimpleLogger(logging)

  inline fun verifyObtainNovels(
      crossinline assertBlock: (List<NovelDetail>) -> Unit = {
        assertTrue("NovelIds is empty", it.isNotEmpty())
        logger.log(it.size)

        it.forEach {
          logger.log(it)
          verifyNovelId(it)
        }
      }
  ) {
    plugin.obtainNovels()
        .map { assertBlock(it) }
        .doOnError { it.printStackTrace() }
        .test().assertNoErrors().assertComplete()
    logger.printlog()
  }

  inline fun verifyObtainChapterIds(
      novelDetail: NovelDetail,
      crossinline assertBlock: (List<ChapterId>) -> Unit = {
        assertTrue("Chapter urls is empty", it.isNotEmpty())
        logger.log(it.size)
        it.forEach {
          logger.log(it)
          verifyChapterId(it)
        }
      }
  ) {
    plugin.obtainChapters(novelDetail)
        .map { assertBlock(it) }
        .test().assertComplete().assertNoErrors()
    logger.printlog()
  }

  inline fun verifyObtainChapterDetail(
      novel: NovelDetail,
      chapter: ChapterId,
      crossinline assertBlock: (ChapterDetail) -> Unit = {
        logger.log(it)
        verifyChapterDetail(it)
      }
  ) {
    plugin.obtainDetail(novel, chapter)
        .map { assertBlock(it) }
        .test()
        .assertNoErrors().assertComplete()
    logger.printlog()
  }

  fun verifyAbsoluteUrl(
      novelDetail: NovelDetail,
      chapterId: ChapterId,
      assertBlock: (String) -> Unit = { assertTrue("Malformed url: $it", HttpUrl.parse(it) != null) }
  ) {
    val url = plugin.toAbsoluteUrl(novelDetail, chapterId)
    logger.log(url)
    assertBlock(url)
    logger.printlog()
  }

  fun verifyIntegration() {
    var novelDetail: NovelDetail? = null
    var chapterId: ChapterId? = null
    var firstChapterId: ChapterId? = null
    var lastChapterId: ChapterId? = null
    val latch = CountDownLatch(1)
    plugin.obtainNovels()
        .doOnSuccess {
          logger.log("1 Novels: ${it.size}")
          assertTrue("Novels is empty", it.isNotEmpty())
        }
        .flatMap {
          plugin.obtainChapters(it.random().also {
            logger.log("2 Novel: $it")
            novelDetail = it
          })
        }
        .doOnSuccess {
          logger.log("3 Chapters: ${it.size}")
          assertTrue("ChapterIds is empty", it.isNotEmpty())
          firstChapterId = it.first()
          lastChapterId = it.last()
        }
        .flatMap {
          plugin.obtainDetail(novelDetail!!, it.middle().also {
            logger.log("4 ChapterId: $it")
            chapterId = it
          })
        }
        .doOnSuccess {
          logger.log("5 Detail->\nNext [${it.nextUrl}]\nPrev [${it.previousUrl}]\nShort raw: ${it.rawText?.substring(0, 100)}..")
          verifyChapterDetail(it)
        }
        .map { it to plugin.toAbsoluteUrl(novelDetail!!, chapterId!!) }
        .doOnSuccess { (detail, currentUrl) ->
          logger.log("6 Curr url: $currentUrl")
          verifyUrlExist(plugin.toAbsoluteUrl(novelDetail!!, CoreChapterId(detail.previousUrl!!, null)))
          verifyUrlExist(currentUrl)
          verifyUrlExist(plugin.toAbsoluteUrl(novelDetail!!, CoreChapterId(detail.nextUrl!!, null)))
        }
        .flatMap {
          plugin.obtainDetail(novelDetail!!, firstChapterId!!)
              .map { logger.log("7 firstChapter: Prev [${it.previousUrl}] Next [${it.nextUrl}]") }
        }.flatMap {
          plugin.obtainDetail(novelDetail!!, lastChapterId!!)
              .map { logger.log("7 lastChapter: Prev [${it.previousUrl}] Next [${it.nextUrl}]") }
        }
        .doOnError { logger.printlog() }
        .doFinally {
          logger.log("8 unlock latch")
          latch.countDown()
        }
        .test().assertNoErrors().assertComplete()
    latch.await(5, TimeUnit.SECONDS)
    logger.log("9 Finished")
    logger.printlog()
  }

  fun verifyUrlExist(url: String) {
    val request = Request.Builder().head().url(url).build()
    TestHelper.okHttpClient()
        .newCall(request)
        .execute()
        .apply {
          assertTrue("Verify URL failure:\n$this", isSuccessful)
          logger.log(this)
        }
  }


  fun verifyNovelId(novel: NovelDetail) {
    with(novel) {
      assertTrue("Title empty : $novel", novelTitle.isNotBlank())
      assertTrue("Url empty : $novel", url.isNotBlank())
    }
  }

  fun verifyChapterId(chapterId: ChapterId) {
    assertTrue("Chapter url is blank:", chapterId.url.isNotBlank())
  }

  fun verifyChapterDetail(detail: ChapterDetail) {
    with(detail) {
      assertFalse("Title text is null/blank:", title.isNullOrBlank())
      assertFalse("Raw text is null/blank:", rawText.isNullOrBlank())
      assertFalse("Next url is null/blank:", nextUrl.isNullOrBlank())
      assertFalse("Previous url is null/blank:", previousUrl.isNullOrBlank())
    }
  }

  private fun <T> List<T>.middle(): T {
    if (isEmpty())
      throw NoSuchElementException("List is empty.")
    return this[size / 2]
  }

  private fun <T> List<T>.random(): T {
    if (isEmpty())
      throw NoSuchElementException("List is empty.")
    return this[Random().nextInt(size)]
  }
}