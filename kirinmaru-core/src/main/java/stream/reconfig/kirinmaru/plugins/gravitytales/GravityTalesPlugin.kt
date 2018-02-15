package stream.reconfig.kirinmaru.plugins.gravitytales

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import stream.reconfig.kirinmaru.core.*
import stream.reconfig.kirinmaru.remote.Providers

/**
 * Gravity Tales plugin
 */
const val GRAVITYTALES_HOME = "http://gravitytales.com/"

class GravityTalesPlugin(override val client: OkHttpClient, override val cookieJar: CookieJar) : Plugin {

  internal val api by lazy {
    Providers.retrofitBuilder(
        GsonBuilder().apply { setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE) }
            .create())
        .baseUrl(GRAVITYTALES_HOME)
        .client(client)
        .build()
        .create(GravityTalesApi::class.java)
  }

  internal val chapterDetailParser by lazy { GravityTalesChapterDetailParser }

  override fun obtainNovels(): Single<List<NovelId>> {
    return api.getNovels().map { flattenResponse(it, GRAVITYTALES_HOME) }
  }

  override fun obtainChapters(novel: NovelId): Single<List<ChapterId>> {
    val id = novel.id ?: throw NullPointerException("NullID = $novel")
    return api.getChapterGroups(id)
        .map { flattenResponse(it, id) }
        .toObservable()
        .concatMapIterable { it } //so we can emit sequentially on each chapter group
        .flatMapSingle { api.getChapterId(it.chapterGroupId) }
        .concatMapIterable { flattenResponse(it, id) }
        .map { it.copy(url = "novel/${novel.url}/${it.url}") }
        .collectInto(ArrayList<ChapterId>()) { t1, t2 -> t1.add(t2) }
        .map { it as List<ChapterId> }
  }

  override fun obtainDetail(chapter: ChapterId): Single<ChapterDetail> {
    return api.getChapterDetail(GravityTalesLinkTransformer.toAbsolute(chapter.url))
        .mapDocument(GRAVITYTALES_HOME)
        .map { chapterDetailParser.parse(it) }
  }
}