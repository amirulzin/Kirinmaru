package stream.reconfig.kirinmaru.android.ui.novels

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.db.NovelDao
import stream.reconfig.kirinmaru.android.ui.favorites.FavoriteNovel
import stream.reconfig.kirinmaru.android.ui.favorites.FavoritePref
import stream.reconfig.kirinmaru.android.util.offline.ResourceContract
import stream.reconfig.kirinmaru.android.util.offline.SimpleResourceLiveData
import stream.reconfig.kirinmaru.android.vo.Novel
import stream.reconfig.kirinmaru.core.NovelDetail
import stream.reconfig.kirinmaru.plugins.PluginMap
import stream.reconfig.kirinmaru.plugins.getPlugin
import javax.inject.Inject

class NovelsLiveData @Inject constructor(
    private val pluginMap: PluginMap,
    private val novelDao: NovelDao,
    private val favoritePref: FavoritePref
) : SimpleResourceLiveData<List<NovelItem>, List<Novel>, List<NovelDetail>>() {

  private val origin = MutableLiveData<String>()

  private val favorites by lazy { favoritePref.load() }

  @MainThread
  fun initOrigin(newOrigin: String) {

    if (origin.value == null) {
      origin.value = newOrigin
    } else {
      origin.value = newOrigin
      refresh()
    }
  }

  @MainThread
  fun toggleFavorite(novelItem: NovelItem, isFavorite: Boolean) {
    if (isFavorite) favorites.add(novelItem.toFavorite())
    else favorites.remove(novelItem.toFavorite())
  }

  override fun onInactive() {
    super.onInactive()
    favoritePref.persist(favorites)
  }

  override fun createContract() =
      object : ResourceContract<List<NovelItem>, List<Novel>, List<NovelDetail>> {
        override fun local(): Flowable<List<Novel>> {
          return novelDao.novelsBy(origin())
        }

        override fun remote(): Single<List<NovelDetail>> {
          return pluginMap.getPlugin(origin()).obtainNovels()
        }

        override fun persist(data: List<NovelDetail>) {
          novelDao.insert(data.map(::toNovel))
        }

        override fun view(local: List<Novel>): List<NovelItem> {
          return local.map(::toNovelItem)
        }
      }

  private fun origin() = origin.value!!

  private fun NovelItem.toFavorite() = FavoriteNovel(origin, url)

  private fun NovelDetail.toFavorite() = FavoriteNovel(origin(), url)

  @WorkerThread
  private fun toNovel(novel: NovelDetail): Novel {
    return Novel(novel.id, novel.novelTitle, novel.url, novel.tags, origin())
  }

  @WorkerThread
  private fun toNovelItem(novel: Novel): NovelItem {
    return NovelItem(novel.id, novel.novelTitle, novel.url, novel.tags, origin(), isFavorite(novel))
  }

  @WorkerThread
  private fun isFavorite(novelDetail: NovelDetail): Boolean {
    return when {
      favorites.isEmpty() -> false
      favorites.contains(novelDetail.toFavorite()) -> true
      else -> false
    }
  }
}