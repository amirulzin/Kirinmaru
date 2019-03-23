package stream.reconfig.kirinmaru.plugins.wuxiaworld

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Response
import stream.reconfig.kirinmaru.core.*
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail
import stream.reconfig.kirinmaru.remote.Providers
import javax.inject.Inject

internal const val WUXIAWORLD_HOME = "http://www.wuxiaworld.com/"

internal const val WUXIAWORLD_ORIGIN = "WuxiaWorld"

/**
 * WuxiaWorld plugin
 */
class WuxiaworldPlugin @Inject constructor(override val client: OkHttpClient, override val cookieJar: CookieJar) : Plugin {

  override val feature: Set<PluginFeature> = emptySet()

  override val origin = WUXIAWORLD_ORIGIN

  private val language = listOf("Chinese", "Korean", "English")
  private val tags = listOf("Completed")

  internal val api by lazy {
    Providers.retrofitBuilder()
        .baseUrl(WUXIAWORLD_HOME)
        .client(client)
        .build()
        .create(WuxiaWorldApi::class.java)
  }

  override fun obtainNovels(searchOptions: SearchOptions): Single<List<NovelDetail>> {

    val obsCompleted = Observable.fromIterable(tags)
        .flatMapSingle(::obtainNovelsByTag)
    val obsLanguage = Observable.fromIterable(language)
        .flatMapSingle(::obtainNovelsByLanguage)

    return Observable.concat(obsCompleted, obsLanguage)
        .collectInto(mutableSetOf<NovelDetail>()) { set, input ->
          for (novel in input) {
            set.find { cached -> cached.url == novel.url }
                ?.let {
                  set.remove(it)
                  val tags = mutableSetOf<String>().apply {
                    addAll(novel.tags)
                    addAll(it.tags)
                  }
                  set.add(CoreNovelDetail(WUXIAWORLD_ORIGIN, it.novelTitle, it.url, it.id, tags))
                } ?: set.add(novel)
          }
        }.map { it.toList() }
  }


  override fun obtainChapters(novelDetail: NovelDetail, searchOptions: SearchOptions): Single<List<ChapterId>> {
    return api.chapters(novelDetail.url)
        .map(::streamToDocument)
        .map(WuxiaWorldChapterIdParser::parse)
  }

  override fun obtainDetail(novelDetail: NovelDetail, chapterId: ChapterId): Single<ChapterDetail> {
    return api.chapter(chapterId.url)
        .map(::streamToDocument)
        .map(WuxiaWorldChapterDetailParser::parse)
  }

  override fun toAbsoluteUrl(novelDetail: NovelDetail, chapterId: ChapterId): String {
    return WuxiaWorldLinkTransformer.toSanitizedAbsolute(chapterId.url)
  }

  private fun obtainNovelsByLanguage(key: String): Single<List<NovelDetail>> {
    return api.novelsByLanguage(key)
        .map(::streamToDocument)
        .map(WuxiaWorldIndexParserV2(key)::parse)
  }

  private fun obtainNovelsByTag(tag: String): Single<List<NovelDetail>> {
    return api.novelsByTag(tag)
        .map(::streamToDocument)
        .map(WuxiaWorldIndexParserV2(tag)::parse)
  }

  private fun streamToDocument(response: Response<ResponseBody>): Document {
    return flattenResponse(response).byteStream()
        .use { Jsoup.parse(it, "UTF-8", WUXIAWORLD_HOME) }
  }
}

