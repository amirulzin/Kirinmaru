package stream.reconfig.kirinmaru.plugins.wuxiaworld

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * WuxiaWorld raw HTTP calls
 */
interface WuxiaWorldApi {

  @GET("language/{language}")
  fun novelsByLanguage(@Path("language") language: String): Single<Response<ResponseBody>>

  @GET("tag/{tag}")
  fun novelsByTag(@Path("tag") tag: String): Single<Response<ResponseBody>>

  @GET
  fun chapters(@Url novelUrl: String): Single<Response<ResponseBody>>

  @GET
  fun chapter(@Url chapterUrl: String): Single<Response<ResponseBody>>
}