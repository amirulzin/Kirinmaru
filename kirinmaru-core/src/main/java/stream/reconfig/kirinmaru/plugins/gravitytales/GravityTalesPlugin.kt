package stream.reconfig.kirinmaru.plugins.gravitytales

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import stream.reconfig.kirinmaru.core.*
import stream.reconfig.kirinmaru.remote.Providers
import java.util.*
import javax.inject.Inject

internal const val GRAVITYTALES_HOME = "http://gravitytales.com/"

internal const val GRAVITYTALES_ORIGIN = "GravityTales"

/**
 * Gravity Tales plugin
 */
class GravityTalesPlugin @Inject constructor(override val client: OkHttpClient, override val cookieJar: CookieJar) : Plugin {

  override val origin = GRAVITYTALES_ORIGIN

  internal val api by lazy {
    Providers.retrofitBuilder(
        GsonBuilder().apply { setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE) }
            .create())
        .baseUrl(GRAVITYTALES_HOME)
        .client(client)
        .build()
        .create(GravityTalesApi::class.java)
  }

  override fun obtainNovels(): Single<List<NovelDetail>> {
    return api.getNovels().map { flattenResponse(it, GRAVITYTALES_HOME).map { it.standardizeUrl() } }
  }

  override fun obtainChapters(novelDetail: NovelDetail): Single<List<ChapterId>> {
    val id = novelDetail.id ?: throw NullPointerException("NullID = $novelDetail")
    return api.getChapterGroups(id)
        .map { flattenResponse(it, id) }
        .toObservable()
        .concatMapIterable { it } //so we can emit sequentially on each chapter group
        .flatMapSingle { api.getChapterId(it.chapterGroupId) }
        .concatMapIterable { flattenResponse(it, id) }
        .collectInto(ArrayList<ChapterId>()) { t1, t2 -> t1.add(t2) }
        .map { it as List<ChapterId> }
  }

  override fun obtainDetail(novelDetail: NovelDetail, chapterId: ChapterId): Single<ChapterDetail> {
    return api.getChapterDetail(toAbsoluteUrl(novelDetail, chapterId))
        .mapDocument(GRAVITYTALES_HOME)
        .map { GravityTalesChapterDetailParser(novelDetail).parse(it) }
  }

  override fun toAbsoluteUrl(novelDetail: NovelDetail, chapterId: ChapterId): String {
    return GravityTalesLinkTransformer.toSanitizedAbsolute("novel", novelDetail.url, chapterId.url)
  }

  /**
   * Standardize GravityTales slugs since it sometimes return in
   * first-letter capitalized slug. This method will return a copy.
   */
  private fun GravityTalesApi.GTNovelDetail.standardizeUrl() = copy(url = url.toLowerCase(Locale.US))
}