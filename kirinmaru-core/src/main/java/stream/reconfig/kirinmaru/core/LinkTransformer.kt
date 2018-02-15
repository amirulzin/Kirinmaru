package stream.reconfig.kirinmaru.core

import okhttp3.HttpUrl

interface LinkTransformer {
  val baseUrl: HttpUrl
  fun toAbsolute(relative: String): String {
    return baseUrl.newBuilder()
        .addPathSegments(relative)
        .build()
        .toString()
  }
}