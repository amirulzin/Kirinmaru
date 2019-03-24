package stream.reconfig.kirinmaru.plugins.wordpress

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.HttpUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * # Retrofit interface for WordPress API v2.
 * Can also be used for sites masquerading as not-a-Wordpress-site in the wild.
 * Discover them by appending `/xmlrpc.php?rsd` to the base domain
 * e.g [http://wuxiaworld.com/xmlrpc.php?rsd](http://wuxiaworld.com/xmlrpc.php?rsd)
 *
 * Some may need proper authorization and matching WordPress plugin installed on their site before access is available.
 * Authentication by cookies is the last fail-safe, provided the site allows registration via `https://sitedomain.com/wp-admin/`
 * Look at [WordPress Authorization](https://developer.wordpress.org/rest-api/using-the-rest-api/authentication/) for more.
 *
 * Reference: [Wordpress REST-API](https://developer.wordpress.org/rest-api/reference/)
 */
interface WordPressApi {

  companion object {
    const val apiPath = "wp-json/wp/v2/"

    fun apiPath(baseUrl: String): HttpUrl = HttpUrl.parse(baseUrl)!!
      .newBuilder()
      .addPathSegments(apiPath)
      .build()
  }

  /**
   * Get collection Pages.
   * All default values match the official WordPress API arguments.
   *
   * @param context   Valid: view, embed, post (post requires valid editing authority)
   * @param pageSize  Result size per page
   * @param page      Page selected. Starts from 1
   * @param parent    Parent id. Useful to find descendants (e.g. valid chapter pages from an index page id)
   * @param orderBy   Valid: author, date, id, include, modified, parent, relevance, slug, title, menu_order
   * @param search    Limit result to certain string. Maybe useful to some.
   * @param slug      Unique page slug. Default to null if undefined.
   * @param order     Valid: desc, asc
   * @param orderBy   Valid: author, date, id, include, modified, parent, relevance, slug, title, menu_order
   */
  @GET("pages")
  fun getPages(
    @Query("context") context: String = "view",
    @Query("per_page") pageSize: Int = 10,
    @Query("page") page: Int = 1, //page selected
    @Query("parent") parent: Long? = null, //parent id
    @Query("search") search: String? = null, //flaky result. Avoid using this if possible.
    @Query("slug") slug: String? = null, //will, and should, always have single result
    @Query("order") order: String = "desc",
    @Query("orderBy") orderBy: String = "date"
  ): Observable<Response<List<Page>>>

  @GET("pages/{id}")
  fun getPage(@Path("id") id: Long): Single<Response<Page>>

  data class Page(
    val id: Long,
    val link: String,
    val slug: String,
    val parent: Long?, //only available in context = 'view'
    val title: Rendered,
    val content: Rendered)

  data class Rendered(val rendered: String)

}