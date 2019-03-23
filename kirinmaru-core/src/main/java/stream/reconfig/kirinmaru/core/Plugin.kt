package stream.reconfig.kirinmaru.core

import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.OkHttpClient

/**
 * Interface that must be implemented for any Plugin.
 *
 * # Rules:
 *
 * * At least one overloaded variant of any methods that defaults to throwing [NotImplementedError] must be implemented
 * * OkHttp [client] is provided, however any Retrofit service must be created manually and delegated by [lazy]
 * * Use GSON for JSON
 * * Different OkHttpClient implementation must use [OkHttpClient.newBuilder] so resources can be pooled across plugins
 * * Written in Kotlin
 *
 * # Code restrictions:
 *
 * * Do not call any Android SDK API. Using Kotlin / Java standard library is fine
 * * Try to use methods/classes/utils from [com.reconfig.kirinmaru.core] as much as possible to avoid duplicates
 */
interface Plugin {
  /**
   * Implement as constructor injection
   */
  val client: OkHttpClient

  /**
   * Implement as constructor injection. The provided shared CookieJar can be used for any authentication black magic
   */
  val cookieJar: CookieJar

  /**
   * The full site URL
   */
  val origin: String

  /**
   * The [PluginFeature] should dictate what optional features is available for the plugin. Can be empty
   */
  val feature: Set<PluginFeature>

  /**
   * Return plugin own implementation of [SearchOptions]. Can be used for paging, genre, tags, etc or empty (default)
   */
  fun searchOptions(): Single<SearchOptions> = Single.just(emptyMap())

  /**
   * Return a list of some [NovelDetail] to be showed to user. Can be empty (default behavior)
   */
  fun obtainPreliminaryNovels(searchOptions: SearchOptions = emptyMap()): Single<List<NovelDetail>> = Single.just(emptyList())

  /**
   * Return a list of [NovelDetail] with optional [SearchOptions]
   */
  fun obtainNovels(searchOptions: SearchOptions = emptyMap()): Single<List<NovelDetail>> = Single.just(emptyList())

  /**
   * Return a list of [ChapterId] with optional [SearchOptions]
   */
  fun obtainChapters(novelDetail: NovelDetail, searchOptions: SearchOptions = emptyMap()): Single<List<ChapterId>> = Single.just(emptyList())

  /**
   * Obtain [ChapterDetail] for the given [ChapterId]
   */
  fun obtainDetail(novelDetail: NovelDetail, chapterId: ChapterId): Single<ChapterDetail>

  /**
   * Obtain absolute url of a [ChapterDetail]. Used when user wanted to visit the actual chapter page
   */
  fun toAbsoluteUrl(novelDetail: NovelDetail, chapterId: ChapterId): String
}