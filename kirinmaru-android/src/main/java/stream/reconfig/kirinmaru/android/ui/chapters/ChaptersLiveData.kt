package stream.reconfig.kirinmaru.android.ui.chapters

import android.arch.lifecycle.MutableLiveData
import commons.android.arch.offline.ResourceContract
import commons.android.arch.offline.SimpleResourceLiveData
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.db.ChapterDao
import stream.reconfig.kirinmaru.android.parcel.NovelParcel
import stream.reconfig.kirinmaru.android.prefs.CurrentReadPref
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.android.vo.DBChapterId
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.plugins.PluginMap
import stream.reconfig.kirinmaru.plugins.getPlugin
import javax.inject.Inject

class ChaptersLiveData @Inject constructor(
  private val pluginMap: PluginMap,
  private val chapterDao: ChapterDao,
  private val currentReadPref: CurrentReadPref
) : SimpleResourceLiveData<List<ChapterItem>, List<DBChapterId>, List<ChapterId>>() {

  private val novel = MutableLiveData<NovelParcel>()

  fun initNovel(novelItem: NovelParcel) {
    novel.value = novelItem
  }

  override fun createContract() =
    object : ResourceContract<List<ChapterItem>, List<DBChapterId>, List<ChapterId>> {
      override fun local(): Flowable<List<DBChapterId>> {
        return novel.value?.let { novel ->
          chapterDao.chaptersAsync(novel.origin, novel.url)
        } ?: Flowable.error(IllegalStateException("Novel null in chapters local()"))
      }

      override fun remote(): Single<List<ChapterId>> {
        return novel.value?.let { novel ->
          pluginMap.getPlugin(novel.origin).obtainChapters(novel)
        } ?: Single.error(IllegalStateException("Novel is null in chapters remote()"))
      }

      override fun persist(data: List<ChapterId>) {
        novel.value?.let { novel ->
          chapterDao.upsert(data.map { Chapter(novel.origin, novel.url, it.url, it.title) })
        }
      }

      override fun view(local: List<DBChapterId>): List<ChapterItem> {
        val currentRead = currentReadPref.load(novel.value!!)
        return local.map { ChapterItem(it.url, it.title, it.url == currentRead?.url) }
          .sortedByDescending { it.taxonomicNumber }
      }

      override fun autoFetch() = true
    }
}