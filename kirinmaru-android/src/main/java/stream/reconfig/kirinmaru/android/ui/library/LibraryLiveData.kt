package stream.reconfig.kirinmaru.android.ui.library

import android.arch.lifecycle.Observer
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
import stream.reconfig.kirinmaru.core.NovelId
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

  private val cache = LibraryCacheLiveData()

  private val cacheObserver = Observer<MutableMap<NovelId, LibraryItem>> {
    it?.let { postValue(it.values.toList()) }
  }

  fun deleteFavorite(favorite: FavoriteNovel) {
    favorites.remove(favorite)
    cache.remove(favorite)
  }

  override fun onActive() {
    super.onActive()
    cache.observeForever(cacheObserver)
    refresh()
  }

  override fun onInactive() {
    super.onInactive()
    cache.removeObserver(cacheObserver)
  }

  override fun refresh() {
    if (resourceState.value?.state != State.LOADING) {
      postLoading()
      disposables.clear()

      local()
          .doOnSuccess {
            cache.update(it)
            postCompleteLocal()
          }
          .toFlowable()
          .onBackpressureBuffer()
          .concatMapIterable { it }
          .parallel(if (favorites.size > 0) favorites.size else 1)
          .runOn(Schedulers.io())
          .flatMap(::remote)
          .sequential()
          .doOnNext(cache::update)
          .subscribeOn(Schedulers.io())
          .observeOn(Schedulers.computation())
          .subscribe(
              { postComplete() },
              { postError(it.message ?: "Fetch Error") }
          ).addTo(disposables)
    }
  }

  /**
   * Sort before any actual posting done
   */
  override fun postValue(value: List<LibraryItem>?) {
    super.postValue(value?.let {
      it.sortedBy { it.novel.novelTitle }
    })
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

  private fun remote(libraryItem: LibraryItem): Flowable<LibraryItem> {
    return pluginMap.getPlugin(libraryItem.novel.origin)
        .obtainChapters(libraryItem.novel)
        .doOnError {
          postErrorRemote(it.message ?: "Remote error for: ${libraryItem.novel.novelTitle}")
        }
        .doOnSuccess { postCompleteRemote() }
        .map {
          it.map { Chapter(libraryItem.novel, it) }
        }
        .doOnSuccess { chapterDao.upsert(it) }
        .map { toLibraryItem(libraryItem.novel, it, isLoading = false) }
        .onErrorReturn { libraryItem.copy(isLoading = false) }
        .toFlowable()
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
        isUpdated = when {
          isLoading -> false
          !isLoading && latestChapter != null && newLatestChapter != null -> latestChapter.url != newLatestChapter.url
          else -> false
        }
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
