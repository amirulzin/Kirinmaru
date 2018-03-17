package stream.reconfig.kirinmaru.android.ui.reader

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.db.ChapterDao
import stream.reconfig.kirinmaru.android.logd
import stream.reconfig.kirinmaru.android.parcel.ChapterIdParcel
import stream.reconfig.kirinmaru.android.parcel.NovelParcel
import stream.reconfig.kirinmaru.android.prefs.CurrentReadPref
import stream.reconfig.kirinmaru.android.util.offline.ResourceContract
import stream.reconfig.kirinmaru.android.util.offline.RxResourceLiveData
import stream.reconfig.kirinmaru.android.util.offline.State
import stream.reconfig.kirinmaru.android.util.rx.addTo
import stream.reconfig.kirinmaru.android.util.textview.HtmlTextUtil
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.core.ChapterDetail
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.taxonomy.Taxonomy
import stream.reconfig.kirinmaru.plugins.PluginMap
import stream.reconfig.kirinmaru.plugins.getPlugin
import javax.inject.Inject

class ReaderLiveData @Inject constructor(
    private val pluginMap: PluginMap,
    private val chapterDao: ChapterDao,
    private val currentReadPref: CurrentReadPref
) : RxResourceLiveData<ReaderDetail>() {

  private val readerData = MutableLiveData<ReaderParcel>()

  private val localObserver = Observer<ReaderParcel> {
    logd("[${resourceState.value}] new ReaderParcel -> Novel: [${it?.novelParcel} Chapter: [${it?.chapterParcel}]")
    refresh()
  }

  private val contract = object : ResourceContract<ReaderDetail, Chapter, ChapterDetail> {
    override fun local(): Flowable<Chapter> {
      return chapterDao.chapterAsync(chapterId().url)
          .doOnNext { logd("local get ${it.url}") }
    }

    override fun remote(): Single<ChapterDetail> {
      return pluginMap.getPlugin(novel().origin)
          .obtainDetail(novel(), chapterId())
          .doOnSuccess { logd("remote found N: [${it.nextUrl}] P: [${it.previousUrl}]") }
    }

    override fun persist(data: ChapterDetail) {
      val chapter = Chapter(
          origin = novel().origin,
          novelUrl = novel().url,
          url = chapterId().url,
          rawText = data.rawText,
          nextUrl = data.nextUrl,
          previousUrl = data.previousUrl)
      log(chapter, "persist")
      chapterDao.upsert(chapter)
    }

    override fun view(local: Chapter): ReaderDetail {
      log(local, "view")
      return local.toReaderDetail()
    }
  }

  @MainThread
  fun initReaderData(data: ReaderParcel) {
    readerData.value = data
  }

  fun navigateNext() {
    value?.nextUrl?.run(::navigateActual)
        ?: postError("Next chapter doesn't exist")

  }

  fun navigatePrevious() {
    value?.previousUrl?.run(::navigateActual)
        ?: postError("Previous chapter doesn't exist")
  }

  fun absoluteUrl(): String? {
    return pluginMap[novel().origin]?.get()?.toAbsoluteUrl(novel(), chapterId())
  }

  override fun onActive() {
    super.onActive()
    readerData.observeForever(localObserver)
  }

  override fun onInactive() {
    super.onInactive()
    readerData.removeObserver(localObserver)
    readerData.value?.let {
      currentReadPref.persist(it.novelParcel, it.chapterParcel.url)
    }
  }

  @SuppressLint("CheckResult")
  override fun refresh() {
    logd("[STATE ${resourceState.value}] Refresh check")
    if (State.LOADING != resourceState.value?.state) {
      postLoading()
      logd("[Disposables ${disposables.size()}] refresh passed. Refreshing")
      disposables.clear()
      logd("[Disposables ${disposables.size()} Cleaned")

      contract.remote()
          .map(contract::persist)
          .subscribeOn(Schedulers.io())
          .observeOn(Schedulers.computation())
          .subscribe({
            logd("COMPLETED REMOTE")
            postComplete()
          }, {
            logd("ERROR REMOTE")
            postError(it.message ?: "Fail to retrieve from network")
          }).addTo(disposables)

      contract.local()
          .map(contract::view)
          .map(::postValue)
          .subscribeOn(Schedulers.io())
          .observeOn(Schedulers.computation())
          .subscribe({
            logd("COMPLETED LOCAL")
            postComplete()
          }, {
            logd("ERROR LOCAL")
            postError(it.message ?: "Fail to fetch local resource")
          }).addTo(disposables)

      logd("[Disposables ${disposables.size()}] Subscribed")
    }
  }

  private fun chapterId(): ChapterId = readerData.value!!.chapterParcel

  private fun novel(): NovelParcel = readerData.value!!.novelParcel

  private fun navigateActual(url: String) {
    logd("Nav to $url")
    readerData.value?.let {
      postValue(null)
      readerData.postValue(it.copy(chapterParcel = ChapterIdParcel(url)))
    } ?: throw IllegalStateException("Novel must be set before any operation")
  }

  @WorkerThread
  private fun Chapter.toReaderDetail() = ReaderDetail(
      text = rawText?.let(HtmlTextUtil::toHtmlSpannable),
      url = url,
      previousUrl = previousUrl,
      nextUrl = nextUrl,
      taxon = Taxonomy.createTaxonomicDisplay(Taxonomy.createTaxonomicNumber(url))
  )

  private fun log(chapter: Chapter?, ops: String = "") {
    logd("State: $ops [${resourceState.value}]" +
        "\nLocal -> Length: [${chapter?.rawText?.length}] Pre: [${chapter?.previousUrl}] Nex: [${chapter?.nextUrl}]" +
        "\nNovel: [${novel().url}]" +
        "\nChapter: [${chapterId().url}] ")
  }
}