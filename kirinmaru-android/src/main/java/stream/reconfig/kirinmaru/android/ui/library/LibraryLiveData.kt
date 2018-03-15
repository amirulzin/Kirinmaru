package stream.reconfig.kirinmaru.android.ui.library

import android.annotation.SuppressLint
import android.support.annotation.WorkerThread
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.db.ChapterDao
import stream.reconfig.kirinmaru.android.db.NovelDao
import stream.reconfig.kirinmaru.android.logOnNext
import stream.reconfig.kirinmaru.android.logOnSuccess
import stream.reconfig.kirinmaru.android.logd
import stream.reconfig.kirinmaru.android.prefs.CurrentReadPref
import stream.reconfig.kirinmaru.android.ui.favorites.FavoriteNovel
import stream.reconfig.kirinmaru.android.ui.favorites.FavoritePref
import stream.reconfig.kirinmaru.android.util.offline.ResourceContract
import stream.reconfig.kirinmaru.android.util.offline.SimpleResourceLiveData
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.android.vo.Novel
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.taxonomy.Taxonomy
import stream.reconfig.kirinmaru.plugins.PluginMap
import stream.reconfig.kirinmaru.plugins.getPlugin
import javax.inject.Inject

private typealias NovelChapters = MutableMap<Novel, List<ChapterId>>

@SuppressLint("CheckResult")
class LibraryLiveData @Inject constructor(
    private val pluginMap: PluginMap,
    private val favoritePref: FavoritePref,
    private val currentReadPref: CurrentReadPref,
    private val novelDao: NovelDao,
    private val chapterDao: ChapterDao
) : SimpleResourceLiveData<List<LibraryItem>, List<LibraryItem>, NovelChapters>() {

  private val favorites by lazy { favoritePref.load() }

  fun deleteFavorite(favorite: FavoriteNovel) = favorites.remove(favorite)

  override fun onActive() {
    super.onActive()
    refresh()
  }

  override fun createContract() = object : ResourceContract<List<LibraryItem>, List<LibraryItem>, NovelChapters> {

    override fun local(): Flowable<List<LibraryItem>> {
      return Flowable.fromCallable { favorites }
          .logOnNext { "Lib Local $it" }
          .concatMap(::asNovels)
          .concatMapIterable { it }
          .flatMap { novel ->
            chapterDao.chaptersBy(novel.url)
                .logOnNext { "local novel urls size ${it.size}" }
                .map { novel to it }
          }
          .logOnNext { "Local success : ${it.first.url} -> ${it.second.size}" }
          .map { (novel, chapterIds) ->
            novel to chapterIds.takeIf { it.isNotEmpty() }
                ?.map { Taxonomy.createTaxonomicNumber(it) }
                ?.first()
          }.map { (novel, latestUrl) -> toLibraryItem(novel, latestUrl) }
          .logOnNext { "novel to collect ${it.novel.url}" }
          .collectInto(mutableListOf<LibraryItem>()) { list, updates ->
            logd("${list.size} Collecting ${updates.novel.url}")
            list.add(updates)
          }
          .logOnSuccess { "success collected ${it.size}" }
          .map { it as List<LibraryItem> }
          .toFlowable()
    }

    override fun remote(): Single<NovelChapters> {
      return Single.fromCallable { favorites }
          .flatMapPublisher(::asNovels)
          .concatMapIterable { it }
          .logOnNext { "Lib Remote load $it" }
          .flatMapSingle { novel ->
            pluginMap.getPlugin(novel.origin)
                .obtainChapters(novel)
                .map { novel to it }
                .logOnSuccess { "Fetch success " }
          }.logOnNext { "Remote fetched ${it.first.url} -> ${it.second.size}" }
          .collectInto(mutableMapOf()) { set, (novel, updates) -> set[novel] = updates }
    }

    override fun persist(data: NovelChapters) {
      data.flatMapTo(mutableListOf()) { (novel, updates) ->
        updates.map { Chapter(origin = novel.origin, novelUrl = novel.url, url = it.url) }
      }.run(chapterDao::insert)
    }

    override fun view(local: List<LibraryItem>): List<LibraryItem> {
      logd("VIEW: ${local.size}")
      return local
    }
  }

  private fun asNovels(storedFavorites: MutableSet<FavoriteNovel>): Flowable<List<Novel>> {
    val origins = mutableSetOf<String>()
    val urls = mutableSetOf<String>()

    storedFavorites.forEach { (origin, url) ->
      origins.add(origin)
      urls.add(url)
    }

    logd("Origins : $origins")
    logd("Urls: $urls")

    return if (urls.isNotEmpty()) novelDao.novelsBy(origins, urls) else Flowable.empty()
  }

  @WorkerThread
  private fun toLibraryItem(novel: Novel, latestUrl: String?): LibraryItem {
    return LibraryItem(
        novel = novel.toNovel(),
        latest = latestUrl?.toChapter(),
        currentRead = currentReadPref.load(novel)?.toChapter()
    )
  }

  private fun Novel.toNovel() = LibraryItem.Novel(origin, url, novelTitle, id, tags)

  private fun String.toChapter() = LibraryItem.Chapter(this)

}
