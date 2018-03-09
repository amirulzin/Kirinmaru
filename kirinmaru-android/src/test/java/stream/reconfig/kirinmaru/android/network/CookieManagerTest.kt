package stream.reconfig.kirinmaru.android.network

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Cookie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private typealias CookieMap = MutableMap<String, MutableList<Cookie>>

/**
 * Serialization test
 */
class CookieManagerTest {

  @Test
  fun persist() {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val map = mutableMapOf<String, MutableList<Cookie>>(
        "test" to mutableListOf(
            Cookie.Builder()
                .name("testName")
                .value("testValue")
                .domain("testDomain")
                .build()))

    val json = gson.toJson(map)
    println(json)
    assertTrue(json.isNotEmpty())

    val type = object : TypeToken<CookieMap>() {}.type

    val mapOut: CookieMap = gson.fromJson(json, type)

    mapOut.forEach { (key, value) ->
      assertEquals(key, "test")
      value.first().apply {
        assertEquals(name(), "testName")
        assertEquals(value(), "testValue")
        assertEquals(domain(), "testDomain".toLowerCase())
      }
    }
  }
}