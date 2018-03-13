package stream.reconfig.kirinmaru.android.ui.reader

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.util.livedata.RxMediatorLiveData
import stream.reconfig.kirinmaru.android.util.rx.addTo
import javax.inject.Inject

class ReaderViewModel @Inject constructor(
    application: Application,
    val reader: ReaderLiveData,
    private val readerPref: ReaderPref
) : AndroidViewModel(application) {

  val readerSetting = object : RxMediatorLiveData<ReaderSetting>() {
    override fun onActive() {
      super.onActive()
      Completable.fromCallable { postValue(readerPref.load()) }
          .subscribeOn(Schedulers.io())
          .subscribe()
          .addTo(disposables)
    }
  }
}
