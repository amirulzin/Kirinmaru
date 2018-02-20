package stream.reconfig.kirinmaru.core

import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.OkHttpClient

/**
 * Interface that must be implemented for any Plugin.
 * Order of method calls can be observed at [PluginContract]
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

  val client: OkHttpClient

  val cookieJar: CookieJar

  fun obtainNovels(): Single<List<NovelId>>

  fun obtainChapters(novel: NovelId): Single<List<ChapterId>> {
    throw NotImplementedError("Not implemented: obtainChapters(novel)")
  }

  fun obtainChapters(novel: NovelId, currentPage: Int): Single<List<ChapterId>> {
    throw NotImplementedError("Not implemented: obtainChapters(novel, currentPage)")
  }

  fun obtainDetail(chapter: ChapterId): Single<ChapterDetail>

  fun toAbsoluteUrl(novelId: NovelId, chapterId: ChapterId): Single<String>
}