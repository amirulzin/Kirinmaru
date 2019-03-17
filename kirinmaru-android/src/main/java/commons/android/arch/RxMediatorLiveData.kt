package commons.android.arch

import android.arch.lifecycle.MediatorLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * MediatorLiveData that calls [CompositeDisposable.clear] when onActive is invoked
 */
abstract class RxMediatorLiveData<T> : MediatorLiveData<T>() {
  protected val disposables = CompositeDisposable()

  fun clearDisposables() = disposables.clear()

  fun ofDisposable(disposable: Disposable) {
    disposables.add(disposable)
  }
}