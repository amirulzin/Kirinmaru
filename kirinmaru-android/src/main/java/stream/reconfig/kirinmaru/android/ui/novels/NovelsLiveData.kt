package stream.reconfig.kirinmaru.android.ui.novels

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import commons.android.arch.offline.ResourceContract
import commons.android.arch.offline.SimpleResourceLiveData
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.db.NovelDao
import stream.reconfig.kirinmaru.android.ui.favorites.FavoriteNovel
import stream.reconfig.kirinmaru.android.ui.favorites.FavoritePref
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

  private val favorites by lazy { favoritePref.loadNonNull() }

  @MainThread
  fun initOrigin(newOrigin: String) {
    origin.value = newOrigin
    if (origin.value != null) {
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
        return novelDao.novelsAsync(origin())
      }

      override fun remote(): Single<List<NovelDetail>> {
        return pluginMap.getPlugin(origin()).obtainNovels()
      }

      override fun persist(data: List<NovelDetail>) {
        novelDao.upsert(data.map(::toNovel))
      }

      override fun view(local: List<Novel>): List<NovelItem> {
        return local.map(::toNovelItem)
      }

      override fun autoFetch() = true
    }

  private fun origin() = origin.value!!

  private fun NovelDetail.toFavorite() = FavoriteNovel(origin(), url, novelTitle, id, tags)

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