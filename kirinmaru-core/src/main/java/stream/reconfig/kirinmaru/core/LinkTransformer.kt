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

  fun toSanitizedAbsolute(vararg unSanitizedSegments: String): String {
    return baseUrl.newBuilder().apply {
      unSanitizedSegments
        .map { it.trim().removePrefix("/").removeSuffix("/").trim() }
        .filter { it.isNotBlank() }
        .forEach { addPathSegments(it) }
    }.build().toString()
  }
}