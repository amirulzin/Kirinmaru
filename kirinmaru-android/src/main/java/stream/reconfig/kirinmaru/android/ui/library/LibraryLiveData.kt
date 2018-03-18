package stream.reconfig.kirinmaru.android.ui.library

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.db.ChapterDao
import stream.reconfig.kirinmaru.android.db.NovelDao
import stream.reconfig.kirinmaru.android.prefs.CurrentReadPref
import stream.reconfig.kirinmaru.android.ui.favorites.FavoriteNovel
import stream.reconfig.kirinmaru.android.ui.favorites.FavoritePref
import stream.reconfig.kirinmaru.android.util.offline.RxResourceLiveData
import stream.reconfig.kirinmaru.android.util.offline.State
import stream.reconfig.kirinmaru.android.util.rx.addTo
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.NovelDetail
import stream.reconfig.kirinmaru.plugins.PluginMap
import stream.reconfig.kirinmaru.plugins.getPlugin
import javax.inject.Inject

class LibraryLiveData @Inject constructor(
    private val pluginMap: PluginMap,
    private val favoritePref: FavoritePref,
    private val currentReadPref: CurrentReadPref,
    private val novelDao: NovelDao,
    private val chapterDao: ChapterDao
) : RxResourceLiveData<List<LibraryItem>>() {

  private val favorites by lazy { favoritePref.load() }

  fun deleteFavorite(favorite: FavoriteNovel) = favorites.remove(favorite)

  override fun onActive() {
    super.onActive()
    refresh()
  }

  override fun refresh() {
    if (resourceState.value?.state != State.LOADING) {
      postLoading()
      disposables.clear()

      local()
          .doOnSuccess {
            postValue(it)
            postCompleteLocal()
          }
          .toFlowable()
          .concatMapIterable { it }
          .flatMapSingle(::remote)
          .collectInto(mutableListOf<LibraryItem>()) { list, item -> list.add(item) }
          .doOnSuccess {
            postValue(it)
            postCompleteRemote()
          }
          .subscribeOn(Schedulers.io())
          .observeOn(Schedulers.computation())
          .subscribe(
              { postComplete() },
              { postError(it.message ?: "Fetch Error") }
          ).addTo(disposables)
    }
  }

  private fun local(): Single<MutableList<LibraryItem>> {
    return Flowable.fromCallable { favorites }
        .onBackpressureBuffer()
        .map(::splitForQuery)
        .map { (origins, urls) -> novelDao.novels(origins, urls) }
        .concatMapIterable { it }
        .map { novel -> novel to chapterDao.chapters(novel.origin, novel.url).map { LibraryItem.Chapter(it) } }
        .map { (novel, chapters) -> toLibraryItem(novel, chapters, isLoading = true) }
        .collectInto(mutableListOf<LibraryItem>()) { list, item -> list.add(item) }
  }

  private fun remote(libraryItem: LibraryItem): Single<LibraryItem> {
    return pluginMap.getPlugin(libraryItem.novel.origin)
        .obtainChapters(libraryItem.novel)
        .doOnError {
          postErrorRemote(it.message ?: "Remote error for: ${libraryItem.novel.novelTitle}")
        }
        .doOnSuccess { postCompleteRemote() }
        .map { it.map { Chapter(libraryItem.novel, it) } }
        .doOnSuccess { chapterDao.upsert(it) }
        .map { toLibraryItem(libraryItem.novel, it, isLoading = false) }
        .onErrorReturn { libraryItem.copy(isLoading = false) }
  }

  private fun splitForQuery(storedFavorites: Set<FavoriteNovel>): Pair<Set<String>, Set<String>> {
    val origins = mutableSetOf<String>()
    val urls = mutableSetOf<String>()
    storedFavorites.forEach { (origin, url) ->
      origins.add(origin)
      urls.add(url)
    }
    return origins to urls
  }

  private fun toLibraryItem(
      novel: NovelDetail,
      chapters: List<ChapterId>,
      latestChapter: LibraryItem.Chapter? = null,
      isLoading: Boolean
  ): LibraryItem {
    val newLatestChapter = findLatest(chapters)
    return LibraryItem(
        novel = novel.toNovel(),
        latest = newLatestChapter ?: latestChapter,
        currentRead = currentReadPref.load(novel)?.toChapter().also { it?.taxonomicNumber },
        isLoading = isLoading,
        isUpdated = newLatestChapter != latestChapter
    )
  }

  private fun findLatest(chapters: List<ChapterId>): LibraryItem.Chapter? {
    return chapters.takeIf { it.isNotEmpty() }
        ?.map { LibraryItem.Chapter(it.url) }
        ?.sortedByDescending { it.taxonomicNumber }
        ?.first()
  }

  private fun NovelDetail.toNovel() = LibraryItem.Novel(origin, url, novelTitle, id, tags)

  private fun String.toChapter() = LibraryItem.Chapter(this)

}
