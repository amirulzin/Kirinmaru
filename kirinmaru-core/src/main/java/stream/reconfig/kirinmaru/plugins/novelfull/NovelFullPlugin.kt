package stream.reconfig.kirinmaru.plugins.novelfull

import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import stream.reconfig.kirinmaru.core.*
import stream.reconfig.kirinmaru.core.PluginFeature.*
import stream.reconfig.kirinmaru.remote.Providers
import javax.inject.Inject

internal const val NOVELFULL_HOME = "http://www.novelfull.com/"

internal const val NOVELFULL_ORIGIN = "NovelFull"

class NovelFullPlugin @Inject constructor(override val client: OkHttpClient, override val cookieJar: CookieJar) : Plugin {

  private val api by lazy {
    Providers.retrofitBuilder()
      .baseUrl(NOVELFULL_HOME)
      .client(client)
      .build()
      .create(NovelFullApi::class.java)
  }

  override val feature: Set<PluginFeature> = setOf(
    COVER_IMAGE,
    CAN_SEARCH_NOVEL,
    PAGED_NOVEL_IDS,
    PAGED_CHAPTER_IDS,
    ALL_CHAPTER_IDS
  )

  override val origin: String = NOVELFULL_ORIGIN

  override fun obtainPreliminaryNovels(searchOptions: SearchOptions): Single<List<NovelDetail>> {
    return api.getHotNovels(searchOptions.getSearchPageOrDefault(1))
      .mapDocument(NOVELFULL_HOME)
      .map(NovelFullSearchParser::parse)
  }

  override fun obtainNovels(searchOptions: SearchOptions): Single<List<NovelDetail>> {
    return api.getNovels(searchOptions.getTerm(), searchOptions.getSearchPageOrDefault(1))
      .mapDocument(NOVELFULL_HOME)
      .map(NovelFullSearchParser::parse)
  }

  override fun obtainChapters(novelDetail: NovelDetail, searchOptions: SearchOptions): Single<List<ChapterId>> {
    return if (searchOptions.isPaged()) {
      searchChaptersPaged(novelDetail, searchOptions)
    } else {
      searchChaptersAll(novelDetail, searchOptions)
    }
  }

  override fun obtainDetail(novelDetail: NovelDetail, chapterId: ChapterId): Single<ChapterDetail> {
    return api.getChapterDetail(chapterId.url)
      .mapDocument(NOVELFULL_HOME)
      .map(NovelFullChapterDetailParser::parse)
  }

  override fun toAbsoluteUrl(novelDetail: NovelDetail, chapterId: ChapterId): String {
    return NovelFullLinkTransformer.toSanitizedAbsolute(chapterId.url)
  }

  private fun searchChaptersAll(novelDetail: NovelDetail, searchOptions: SearchOptions): Single<List<ChapterId>> {
    return api.getChapterListPaged(novelDetail.url)
      .mapDocument(NOVELFULL_HOME)
      .map(NovelFullChapterIdParser::parse) //get some chapters
      .map { list -> list.random() }
      .flatMap { chapterId -> api.getChapterDetail(chapterId.url) } //get chapter detail
      .mapDocument(NOVELFULL_HOME)
      .map(NovelFullNovelIdParser::parse) //parse novel id (since it's only available in the chapter texts)
      .map(NovelFullNovelIdParser::validateId)
      .flatMap(api::getChapterListAll)
      .mapDocument(NOVELFULL_HOME)
      .map(NovelFullAllChapterIdParser::parse) // parse to actual chapterId
  }

  private fun searchChaptersPaged(novelDetail: NovelDetail, searchOptions: SearchOptions): Single<List<ChapterId>> {
    var page = searchOptions.getSearchPageOrDefault(1)
    when {
      searchOptions.containsKey(SearchKeys.Direction.NEXT) -> page += 1
      searchOptions.containsKey(SearchKeys.Direction.PREVIOUS) -> page -= 1
    }
    return api.getChapterListPaged(novelDetail.url, page)
      .mapDocument(NOVELFULL_HOME)
      .map(NovelFullChapterIdParser::parse)
  }
}