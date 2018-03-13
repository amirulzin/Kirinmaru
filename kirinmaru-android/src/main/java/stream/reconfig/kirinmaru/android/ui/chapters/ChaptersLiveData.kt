package stream.reconfig.kirinmaru.android.ui.chapters

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.db.ChapterDao
import stream.reconfig.kirinmaru.android.ui.novels.NovelItem
import stream.reconfig.kirinmaru.android.util.offline.ResourceContract
import stream.reconfig.kirinmaru.android.util.offline.ResourceLiveData
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.plugins.PluginMap
import javax.inject.Inject

class ChaptersLiveData @Inject constructor(
    private val pluginMap: PluginMap,
    private val chapterDao: ChapterDao
) : ResourceLiveData<List<ChapterItem>, List<String>, List<ChapterId>>() {

  private val novel = MutableLiveData<NovelItem>()

  fun initNovel(novelItem: NovelItem) {
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
            pluginMap[novel.origin]?.get()?.obtainChapters(novel)
          } ?: Single.error(IllegalStateException("No plugin match for ${novel.value}"))
        }

        override fun transform(remote: List<ChapterId>): List<String> {
          return remote.map { it.url }
        }

        override fun persist(data: List<String>) {
          novel.value?.run {
            chapterDao.insert(data.map { Chapter(this.url, it) })
          }
        }

        override fun view(local: List<String>): List<ChapterItem> {
          return local.map { ChapterItem(it) }
              .sortedByDescending { it.taxonomicNumber }
        }
      }
}