package stream.reconfig.kirinmaru.core

import okhttp3.HttpUrl

interface LinkTransformer {
  val baseUrl: HttpUrl
  fun toAbsolute(pathSegments: String): String {
    return baseUrl.newBuilder()
        .addPathSegments(pathSegments)
        .build()
        .toString()
  }
}