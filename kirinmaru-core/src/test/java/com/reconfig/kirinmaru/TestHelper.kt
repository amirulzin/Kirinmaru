package com.reconfig.kirinmaru

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.io.IOUtils

/**
 * TestHelper
 */
object TestHelper {
  fun readResource(path: String): String {
    TestHelper::class.java.classLoader
        .getResourceAsStream(path)
        .use { return IOUtils.toString(it, Charsets.UTF_8) }
  }

  fun okHttpClient(level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(level))
        .build()
  }
}