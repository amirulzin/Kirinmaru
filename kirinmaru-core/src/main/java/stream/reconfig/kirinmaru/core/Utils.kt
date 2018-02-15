package stream.reconfig.kirinmaru.core

import io.reactivex.Single
import okhttp3.ResponseBody
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Response
import stream.reconfig.kirinmaru.core.parser.ParseException
import java.io.IOException

/**
 * Jsoup Util for selecting non empty elements
 */
@Nullable
internal inline fun <T> Document.selectBy(selector: String, crossinline block: (Elements) -> T?): T? {
  return select(selector)
      .takeIf { it.isNotEmpty() }
      ?.let { block(it) }
}

@NotNull
internal fun Single<Response<ResponseBody>>.mapDocument(baseUri: String): Single<Document> {
  return this.map {
    if (it.isSuccessful) {
      it.body()
          ?.byteStream()
          ?.use { Jsoup.parse(it, "UTF-8", baseUri) }
          ?: throw ParseException("Body is null/empty", baseUri)
    } else throw IOException("Origin: $baseUri\nError: ${it.message()}\nBody: ${it.errorBody()}")

  }
}

@NotNull
internal fun <T> flattenResponse(response: Response<T>, origin: String = ""): T {
  return if (response.isSuccessful)
    response.body()!!
  else throw IOException("Origin: $origin\nError: ${response.message()}\nBody: ${response.errorBody()}")
}

