package stream.reconfig.kirinmaru.android.ui.novels

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.db.NovelDao
import stream.reconfig.kirinmaru.android.prefs.FavoritePref
import stream.reconfig.kirinmaru.android.util.offline.ResourceContract
import stream.reconfig.kirinmaru.android.util.offline.ResourceLiveData
import stream.reconfig.kirinmaru.android.vo.Novel
import stream.reconfig.kirinmaru.core.NovelId
import stream.reconfig.kirinmaru.plugins.PluginMap
import javax.inject.Inject

class NovelsLiveData @Inject constructor(
    private val pluginMap: PluginMap,
    private val novelDao: NovelDao,
    private val favoritePref: FavoritePref
) : ResourceLiveData<List<NovelItem>, List<Novel>, List<NovelId>>() {

  private val origin = MutableLiveData<String>()

  private val favorites by lazy { favoritePref.load() }

  @MainThread
  fun initOrigin(origin: String) {
    this.origin.value = origin
  }

  @MainThread
  fun toggleFavorite(novelId: NovelId, favorited: Boolean) {
    if (favorited) favorites.add(novelId.url)
    else favorites.remove(novelId.url)
  }

  override fun onInactive() {
    super.onInactive()
    favoritePref.persist(favorites)
  }

  override fun createContract() =
      object : ResourceContract<List<NovelItem>, List<Novel>, List<NovelId>> {
        override fun local(): Flowable<List<Novel>> {
          return origin.value?.let { novelDao.novelsBy(it) }
              ?: Flowable.error(IllegalStateException("Novels: Origin not initialized"))
        }

        override fun remote(): Single<List<NovelId>> {
          return origin.value?.let {
            pluginMap[it]?.get()?.obtainNovels()
                ?: Single.error(IllegalStateException("Novels: Plugin not recognized: $it"))
          } ?: Single.error(IllegalStateException("Novels: Origin not initialized"))
        }

        override fun transform(remote: List<NovelId>): List<Novel> {
          return remote.map(::toNovel)
        }

        override fun persist(data: List<Novel>) {
          novelDao.insert(data)
        }

        override fun view(local: List<Novel>): List<NovelItem> {
          return local.map(::toNovelItem)
        }
      }

  @WorkerThread
  private fun toNovel(novelId: NovelId): Novel {
    return Novel(novelId.id, novelId.novelTitle, novelId.url, novelId.tags, origin.value!!)
  }

  @WorkerThread
  private fun toNovelItem(novel: Novel): NovelItem {
    return NovelItem(novel.id, novel.novelTitle, novel.url, novel.tags, origin.value!!, isFavorite(novel))
  }

  @WorkerThread
  private fun isFavorite(novelId: NovelId): Boolean {
    return when {
      favorites.isEmpty() -> false
      favorites.contains(novelId.url) -> true
      else -> false
    }
  }
}