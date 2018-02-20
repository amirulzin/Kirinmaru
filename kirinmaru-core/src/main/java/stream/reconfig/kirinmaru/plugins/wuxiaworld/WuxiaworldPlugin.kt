package stream.reconfig.kirinmaru.plugins.wuxiaworld

import io.reactivex.Single
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import stream.reconfig.kirinmaru.core.*
import stream.reconfig.kirinmaru.core.domain.CoreChapterDetail
import stream.reconfig.kirinmaru.core.domain.CoreChapterId
import stream.reconfig.kirinmaru.core.parser.Parser
import stream.reconfig.kirinmaru.plugins.wordpress.WordPressApi
import stream.reconfig.kirinmaru.remote.Providers
import java.io.IOException
import javax.inject.Inject

/**
 * WuxiaWorld plugin
 */
internal const val WUXIAWORLD_HOME = "https://www.wuxiaworld.com/"

class WuxiaworldPlugin @Inject constructor(override val client: OkHttpClient, override val cookieJar: CookieJar) : Plugin {

  internal val wpApi by lazy {
    Providers.retrofitBuilder()
        .baseUrl(WordPressApi.apiPath(WUXIAWORLD_HOME))
        .client(client.newBuilder()
            .cookieJar(cookieJar)
            .build())
        .build()
        .create(WordPressApi::class.java)
  }

  override fun obtainNovels(): Single<List<NovelId>> {
    return Single.fromCallable {
      val response = getResponse(WUXIAWORLD_HOME)
      if (response.isSuccessful) {
        response.body()
            ?.byteStream()
            ?.use { Jsoup.parse(it, "UTF-8", WUXIAWORLD_HOME) }
            ?.let { WuxiaWorldIndexParser.parse(it) }
            ?: throw IOException("Null body for $WUXIAWORLD_HOME")
      } else throw IOException("Response error on Index: $WUXIAWORLD_HOME\n $response")
    }
  }

  override fun obtainChapters(novel: NovelId, currentPage: Int): Single<List<ChapterId>> {

    val sNovelId = novel.id?.let { Single.just(it) } ?: findNovelId(novel)

    return sNovelId.flatMap {
      val parent = it.toLong()
      //Handle if matched with old novels first
      OldNovels.map.findKeys(novel.url)
          ?.map { CoreChapterId(it) }
          ?.toList()
          ?.let { Single.just(it) }
          ?: wpApi.getPages(context = "embed", pageSize = 5, parent = parent, page = currentPage)
              .concatMapIterable { flattenResponse(it, novel.url) }
              .collectInto(ArrayList<ChapterId>()) { list, item -> list.add(item.toChapterId()) }

    }
  }

  override fun obtainDetail(chapter: ChapterId): Single<ChapterDetail> {
    return wpApi.getPages(slug = chapter.url)
        .concatMapIterable { flattenResponse(it, chapter.url) }
        .firstOrError()
        .map { it.toChapterDetail() }
  }

  override fun toAbsoluteUrl(novelId: NovelId, chapterId: ChapterId): Single<String> {
    return Single.fromCallable { HttpUrl.parse(novelId.url)!!.newBuilder().addPathSegment(chapterId.url).toString() }
  }

  private fun WordPressApi.Page.toChapterId() = CoreChapterId(slug)

  private fun WordPressApi.Page.toChapterDetail(): CoreChapterDetail {
    val body = content.rendered
    val document = Jsoup.parseBodyFragment(body)
    return CoreChapterDetail(
        rawText = body,
        nextUrl = document.selectBy("a:containsOwn(next)") { it.first().attr("href") },
        previousUrl = document.selectBy("a:containsOwn(previous)") { it.first().attr("href") }
    )
  }

  private fun String.toHttpUrl() = HttpUrl.parse(this)!!

  private fun <V> Map<String, V>.findKeys(url: String): V? {
    return this.keys.find { url.contains(it) }?.let { this[it] }
  }

  private fun getResponse(url: String): Response {
    return client.newCall(Request.Builder().url(url).get().build()).execute()
  }

  private fun findNovelId(novel: NovelId): Single<String> {
    val slug = novel.url
        .toHttpUrl()
        .pathSegments()
        .last { it.isNotBlank() }

    return wpApi.getPages(slug = slug, context = "embed")
        .map { flattenResponse(it, slug).find { slug.equals(it.slug, true) } }
        .singleOrError()
        .map { it.id.toString() }
  }

  internal object WuxiaWorldIndexParser : Parser<List<NovelId>> {

    private fun selectByKey(key: String) = "#menu-home-menu > li:has(a[href=#]:containsOwn($key)) a:not(a[href=#])"

    override fun parse(document: Document): List<NovelId> {
      return arrayOf("Completed", "Chinese", "Korean", "Originals")
          .flatMap { key ->
            document.select(selectByKey(key)).map { toNovelId(it, key) }
          }
    }

    private fun toNovelId(ele: Element, key: String): WWNovelId {
      val title = ele.ownText()
      val tags = mutableSetOf(key)
      when {
        title.contains("[KR]") -> tags.add("Korean")
        key != "Korean" -> tags.add("Chinese")
      }
      return WWNovelId(title, ele.attr("href"), null, tags.toSet()) //id is set when chapter list is retrieved
    }
  }

  internal data class WWNovelId(
      override val novelTitle: String,
      override val url: String,
      override val id: String? = null,
      override val tags: Set<String> = emptySet()
  ) : NovelId
}

