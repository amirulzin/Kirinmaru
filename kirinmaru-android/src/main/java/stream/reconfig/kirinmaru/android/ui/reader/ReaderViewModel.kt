package stream.reconfig.kirinmaru.android.ui.reader

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.util.livedata.RxMediatorLiveData
import stream.reconfig.kirinmaru.android.util.rx.addTo
import javax.inject.Inject

class ReaderViewModel @Inject constructor(
    application: Application,
    val reader: ReaderLiveData,
    private val readerPref: ReaderPref
) : AndroidViewModel(application) {

  private lateinit var readerParcel: ReaderParcel

  val readerSetting = object : RxMediatorLiveData<ReaderSetting>() {
    override fun onActive() {
      super.onActive()
      Single.fromCallable {
        readerPref.load(readerParcel.novelParcel).let {
          when {
            it.isGlobal -> ReaderSetting.default(application)
            else -> it
          }
        }
      }.map(::postValue)
          .subscribeOn(Schedulers.io())
          .subscribe()
          .addTo(disposables)
    }

    override fun onInactive() {
      super.onInactive()
      value?.let { readerPref.persist(readerParcel.novelParcel, it) }
    }
  }

  fun initReader(readerParcel: ReaderParcel) {
    this.readerParcel = readerParcel
    reader.initReaderData(readerParcel)
  }
}
