package com.reconfig.kirinmaru.plugins.wuxiaworld

import com.reconfig.kirinmaru.TestHelper
import org.jsoup.Jsoup
import org.junit.Assert.assertTrue
import org.junit.Test
import stream.reconfig.kirinmaru.plugins.wuxiaworld.WuxiaworldPlugin

/**
 * Test for parsing WuxiaWorld Index
 */
class WuxiaWorldIndexParserTest {
  @Test
  fun parse() {
    val resource = TestHelper.readResource("wuxiaworld-home.html")
    val document = Jsoup.parse(resource)
    val list = WuxiaworldPlugin.WuxiaWorldIndexParser.parse(document)
    list.forEach {
      println(it)
      with(it) {
        assertTrue(url.isNotBlank())
        assertTrue(novelTitle.isNotBlank())
        assertTrue(tags.isNotEmpty())
      }
    }
  }
}