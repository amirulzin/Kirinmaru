package stream.reconfig.kirinmaru.plugins.wuxiaworld

import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import stream.reconfig.kirinmaru.TestHelper
import stream.reconfig.kirinmaru.core.flattenResponse
import stream.reconfig.kirinmaru.remote.Providers

/**
 * Quick test to validate novels retrieval
 */
@Ignore("Unused")
class WuxiaWorldIndexParserTest {

  @Test
  fun parse() {
    val key = "Completed"
    val client = TestHelper.okHttpClient(HttpLoggingInterceptor.Level.BASIC)
    val api = Providers.retrofitBuilder()
        .baseUrl(WUXIAWORLD_HOME)
        .client(client)
        .build()
        .create(WuxiaWorldApi::class.java)

    api.novelsByTag(key)
        .map { flattenResponse(it) }
        .map {
          it.byteStream().use {
            Jsoup.parse(it, "UTF-8", WUXIAWORLD_HOME)
          }
        }.map {
          WuxiaWorldIndexParserV2(key).parse(it)
        }
        .map {
          assertTrue("Novel list is empty", it.isNotEmpty())
          it.forEach(::println)
        }.blockingGet()
  }

}