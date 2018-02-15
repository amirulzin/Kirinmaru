package com.reconfig.kirinmaru.plugins.wuxiaworld

import com.reconfig.kirinmaru.TestHelper
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.NovelId
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.domain.CoreNovelId
import stream.reconfig.kirinmaru.plugins.wuxiaworld.WuxiaworldPlugin

/**
 * Test for WuxiaWorldPlugin
 */
open class WuxiaworldPluginTest {

  val plugin = WuxiaworldPlugin(
      TestHelper.okHttpClient(HttpLoggingInterceptor.Level.BASIC)
          .newBuilder()
          .build(), CookieMonster())

  class CookieMonster : CookieJar {

    //Cookie of a test account. Though we'll look into storing authorization cookie for official users

    val testAuthCookie = Cookie.Builder().apply {
      name("wordpress_logged_in_684741d2b57c8ac0facd95de1fc9e388")
      value("dejavu%7C1519672920%7Cqy7OEB7qAN9cl03uqI8DvAvx4kwNNSh2PHPgvYqiwIS%7C4a38513ae9422c3e90a220e3c880eb213410f61ecbb6ddf62bb7d7364bd68112")
      domain("www.wuxiaworld.com")
    }.build()

    private val map = mutableMapOf<String, MutableList<Cookie>>("wuxiaworld.com" to mutableListOf(testAuthCookie))

    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
      url?.topPrivateDomain()?.let { domain ->
        cookies?.let {
          map[domain]?.addAll(it)
          //println("saved: \n$map\n")
        }
      }
    }

    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
      return (url?.let { map[it.topPrivateDomain()] }
          ?: mutableListOf()).apply {
        this.forEach {
          //    println("loaded: $it")
        }
      }
    }
  }

  @Test
  fun obtainNovels() {
    plugin.obtainNovels()
        .map {
          assertTrue(it.isNotEmpty())
          it.onEach {
            assertTrue(it.url.isNotBlank())
            assertTrue(it.id == null)
            println(it.url)
          }
        }.test().assertNoErrors().assertComplete()
  }

  @Test
  fun obtainChapters() {
    var error: NotImplementedError? = null
    try {
      plugin.obtainChapters(CoreNovelId("Wu Dong Qian Kun", "http://www.wuxiaworld.com/wdqk-index/", null))
    } catch (e: NotImplementedError) {
      error = e
    }
    assertTrue(error != null)
  }

  @Test
  fun obtainChaptersWithPaging() {
    //url as returned by obtainNovels()
    plugin.obtainChapters(CoreNovelId("Wu Dong Qian Kun", "http://www.wuxiaworld.com/wdqk-index/", null), 1)
        .map {
          assertTrue(it.isNotEmpty())
          it.onEach {
            assertTrue(it.url.isNotBlank())
            println(it.url)
          }
        }.test().assertNoErrors().assertComplete()
  }

  @Test
  fun obtainDetail() {
    plugin.obtainDetail(CoreChapterId("wdqk-chapter-984"))
        .map {
          with(it) {
            assertTrue(rawText!!.isNotBlank())
            assertTrue(nextUrl!!.isNotBlank())
            assertTrue(previousUrl!!.isNotBlank())
          }
        }.test().assertNoErrors().assertComplete()
  }

  @Ignore("Heavy call to the API. Only to verify each novel have at least one chapter")
  @Test
  fun indexToChaptersIntegrationTest() {
    var novelCount = 0
    val errata = mutableListOf<String>()
    plugin.obtainNovels()
        .map {
          novelCount = it.size
          it
        }
        .toObservable()
        .flatMapIterable { it }
        .flatMap { novel -> plugin.obtainChapters(novel, 1).map { novel to it }.toObservable() }
        .collectInto(ArrayList<Pair<NovelId, List<ChapterId>>>()) { list, item -> list.add(item) }
        .map {
          it.onEach {
            println(it.first)
            it.second.forEach { println(it.url) }
            if (it.second.isEmpty()) errata.add(it.first.novelTitle)
          }
          assertTrue("Size mismatch", novelCount == it.size)
        }.test()
        .assertNoErrors()
        .assertComplete()
    assertTrue("Chapters is empty: ${errata.onEach { println(it) }}", errata.isEmpty())
  }
}