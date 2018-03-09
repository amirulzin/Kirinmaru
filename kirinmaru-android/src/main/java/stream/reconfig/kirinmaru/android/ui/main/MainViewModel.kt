package stream.reconfig.kirinmaru.android.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.util.livedata.RxMediatorLiveData
import stream.reconfig.kirinmaru.android.util.rx.addTo
import javax.inject.Inject

/**
 * ViewModel for the single main activity. Allow access to AppState live data.
 */
class MainViewModel @Inject constructor(
    application: Application,
    private val firstAppState: AppState
) : AndroidViewModel(application) {
  val appState = object : RxMediatorLiveData<AppState>() {
    override fun onActive() {
      super.onActive()
      if (value == null) {
        Single.fromCallable { firstAppState.apply { initStates() } }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::postValue)
            .addTo(disposables)
      }
    }
  }
}