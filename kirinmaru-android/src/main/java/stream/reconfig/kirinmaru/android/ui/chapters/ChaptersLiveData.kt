package stream.reconfig.kirinmaru.android.ui.chapters

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.db.ChapterDao
import stream.reconfig.kirinmaru.android.parcel.NovelParcel
import stream.reconfig.kirinmaru.android.prefs.CurrentReadPref
import stream.reconfig.kirinmaru.android.util.offline.ResourceContract
import stream.reconfig.kirinmaru.android.util.offline.ResourceLiveData
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.plugins.PluginMap
import stream.reconfig.kirinmaru.plugins.getPlugin
import javax.inject.Inject

class ChaptersLiveData @Inject constructor(
    private val pluginMap: PluginMap,
    private val chapterDao: ChapterDao,
    private val currentReadPref: CurrentReadPref
) : ResourceLiveData<List<ChapterItem>, List<String>, List<ChapterId>>() {

  private val novel = MutableLiveData<NovelParcel>()

  fun initNovel(novelItem: NovelParcel) {
    novel.value = novelItem
  }

  override fun createContract() =
      object : ResourceContract<List<ChapterItem>, List<String>, List<ChapterId>> {

        override fun local(): Flowable<List<String>> {
          return novel.value?.let { novel ->
            chapterDao.chaptersBy(novel.url)
          } ?: Flowable.error(IllegalStateException("Novel null in chapters local()"))
        }

        override fun remote(): Single<List<ChapterId>> {
          return novel.value?.let { novel ->
            pluginMap.getPlugin(novel.origin).obtainChapters(novel)
          } ?: Single.error(IllegalStateException("Novel is null in chapters remote()"))
        }

        override fun persist(data: List<ChapterId>) {
          novel.value?.run {
            chapterDao.insertLatest(data.map { Chapter(origin, url, it.url) })
          }
        }

        override fun view(local: List<String>): List<ChapterItem> {
          val currentRead = currentReadPref.load(novel.value!!)
          return local.map { ChapterItem(it, it == currentRead) }
              .sortedByDescending { it.taxonomicNumber }
        }
      }
}