package commons.android.arch

import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable

/**
 * MediatorLiveData that calls [CompositeDisposable.clear] when onActive is invoked
 */
abstract class RxMediatorLiveData<T> : MediatorLiveData<T>() {
  protected val disposables = CompositeDisposable()

  @CallSuper
  override fun onInactive() {
    super.onInactive()
    disposables.clear()
  }
}