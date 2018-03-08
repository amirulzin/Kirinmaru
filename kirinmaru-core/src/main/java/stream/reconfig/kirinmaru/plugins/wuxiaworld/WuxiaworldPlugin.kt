package stream.reconfig.kirinmaru.plugins.wuxiaworld

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Response
import stream.reconfig.kirinmaru.core.*
import stream.reconfig.kirinmaru.core.domain.CoreNovelId
import stream.reconfig.kirinmaru.remote.Providers
import javax.inject.Inject

/**
 * WuxiaWorld plugin
 */
class WuxiaworldPlugin @Inject constructor(override val client: OkHttpClient, override val cookieJar: CookieJar) : Plugin {

  private val language = listOf("Chinese", "Korean", "English")
  private val tags = listOf("Completed")

  internal val api by lazy {
    Providers.retrofitBuilder()
        .baseUrl(WUXIAWORLD_HOME)
        .client(client)
        .build()
        .create(WuxiaWorldApi::class.java)
  }

  override fun obtainNovels(): Single<List<NovelId>> {

    val obsCompleted = Observable.fromIterable(tags)
        .flatMapSingle(::obtainNovelsByTag)
    val obsLanguage = Observable.fromIterable(language)
        .flatMapSingle(::obtainNovelsByLanguage)

    return Observable.concat(obsCompleted, obsLanguage)
        .collectInto(mutableSetOf<NovelId>()) { set, input ->
          for (novel in input) {
            set.find { cached -> cached.url == novel.url }
                ?.let {
                  set.remove(it)
                  val tags = mutableSetOf<String>().apply {
                    addAll(novel.tags)
                    addAll(it.tags)
                  }
                  set.add(CoreNovelId(it.novelTitle, it.url, it.id, tags))
                } ?: set.add(novel)
          }
        }.map { it.toList() }
  }


  override fun obtainChapters(novel: NovelId): Single<List<ChapterId>> {
    return api.chapters(novel.url)
        .map(::streamToDocument)
        .map(WuxiaWorldChapterIdParser::parse)
  }

  override fun obtainDetail(chapter: ChapterId): Single<ChapterDetail> {
    return api.chapter(chapter.url)
        .map(::streamToDocument)
        .map(WuxiaWorldChapterDetailParser::parse)
  }

  override fun toAbsoluteUrl(novelId: NovelId, chapterId: ChapterId): Single<String> {
    return Single.fromCallable {
      HttpUrl.parse(WUXIAWORLD_HOME)!!
          .newBuilder()
          .addPathSegments(chapterId.url)
          .toString()
    }
  }

  private fun obtainNovelsByLanguage(key: String): Single<List<NovelId>> {
    return api.novelsByLanguage(key)
        .map(::streamToDocument)
        .map(WuxiaWorldIndexParserV2(key)::parse)
  }

  private fun obtainNovelsByTag(tag: String): Single<List<NovelId>> {
    return api.novelsByTag(tag)
        .map(::streamToDocument)
        .map(WuxiaWorldIndexParserV2(tag)::parse)
  }

  private fun streamToDocument(response: Response<ResponseBody>): Document {
    return flattenResponse(response).byteStream()
        .use { Jsoup.parse(it, "UTF-8", WUXIAWORLD_HOME) }
  }
}

