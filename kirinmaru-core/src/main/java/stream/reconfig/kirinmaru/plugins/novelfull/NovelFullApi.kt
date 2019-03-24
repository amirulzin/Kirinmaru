package stream.reconfig.kirinmaru.plugins.novelfull

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NovelFullApi {

  @GET("hot-novel")
  fun getHotNovels(@Query("page") page: Int = 1): Single<Response<ResponseBody>>

  @GET("search")
  fun getNovels(@Query("keyword") keyword: String?, @Query("page") page: Int = 1): Single<Response<ResponseBody>>

  @GET
  fun getChapterListPaged(@Url novelUrl: String, @Query("page") page: Int = 1): Single<Response<ResponseBody>>

  @GET("ajax-chapter-option")
  fun getChapterListAll(@Query("novelId") novelId: Long): Single<Response<ResponseBody>>

  @GET
  fun getChapterDetail(@Url chapterUrl: String): Single<Response<ResponseBody>>
}