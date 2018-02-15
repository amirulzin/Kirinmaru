package com.reconfig.kirinmaru.plugins.wuxiaworld

import io.reactivex.Observable
import org.junit.Assert.assertTrue
import org.junit.Test
import stream.reconfig.kirinmaru.core.flattenResponse
import stream.reconfig.kirinmaru.plugins.wordpress.WordPressApi
import stream.reconfig.kirinmaru.plugins.wuxiaworld.OldNovels

/**
 * Test for WuxiaWorld novel with parent ID = 0
 */
class WuxiaWorldOldNovelsTest : WuxiaworldPluginTest() {

  @Test
  fun obtainChapterList() {
    val knownId = 4893L
    var currSize = 0
    var pageCount = 0
    Observable.fromCallable { pageCount }
        .flatMap { plugin.wpApi.getPages(parent = knownId, context = "embed", page = ++pageCount) }
        .map { flattenResponse(it) }
        .doOnNext { currSize = it.size }
        .repeatUntil { currSize < 10 }
        .collectInto(ArrayList<WordPressApi.Page>()) { t1, t2 -> t1.addAll(t2) }
        .map {
          it.groupBy {
            it.slug.subSequence(0, 2)
          }.mapKeys { println("Key:");println(it.key); it.value.onEach { println(it.slug) } }
        }
        .test().assertNoErrors().assertComplete()
  }

  @Test
  fun testFind() {

    fun <V> Map<String, V>.findKeys(url: String): V? {
      return this.keys.find { url.contains(it) }?.let { this[it] }
    }

    arrayOf("http://www.wuxiaworld.com/7-killers/",
        "http://www.wuxiaworld.com/hsnt-index/",
        "http://www.wuxiaworld.com/dkwss/").forEach {
      println(OldNovels.map.findKeys(it)!!.size)
      assertTrue(it.isNotEmpty())
    }

  }
}