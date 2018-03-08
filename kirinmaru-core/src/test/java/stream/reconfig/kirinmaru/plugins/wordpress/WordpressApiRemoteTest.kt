package stream.reconfig.kirinmaru.plugins.wordpress

import stream.reconfig.kirinmaru.TestHelper
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import stream.reconfig.kirinmaru.core.flattenResponse
import stream.reconfig.kirinmaru.plugins.wordpress.WordPressApi
import stream.reconfig.kirinmaru.remote.Providers

/**
 * Integration test for WordPress API based on the official demo site
 */
@Ignore("Remote")
class ordpressApiRemoteTest {

  val baseUrl = "https://demo.wp-api.org/"
  val api = Providers.retrofitBuilder()
      .baseUrl(WordPressApi.apiPath(baseUrl))
      .client(TestHelper.okHttpClient(HttpLoggingInterceptor.Level.BASIC))
      .build()
      .create(WordPressApi::class.java)

  @Test
  fun testPathBuilder() {
    val actual = "https://demo.wp-api.org/wp-json/wp/v2/"
    assertTrue(actual.contentEquals(WordPressApi.apiPath(baseUrl).toString()))
  }

  @Test
  fun testPages() {
    val samplePageId: Long = 2
    api.getPage(samplePageId).map {
      flattenResponse(it).apply {
        assertTrue(id > -1)
        assertTrue(link.isNotBlank())
        assertTrue(slug.isNotBlank())
        assertTrue(title.rendered.isNotBlank())
        assertTrue(content.rendered.isNotBlank())
      }
    }.test().assertComplete().assertNoErrors()
  }
}