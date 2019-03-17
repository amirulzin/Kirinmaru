package commons.android.arch

import android.support.annotation.CallSuper
import commons.android.arch.offline.RxResourceLiveData
import java.util.concurrent.TimeUnit

@Suppress("MemberVisibilityCanBePrivate")
abstract class AutoRemoteLiveData<T>(protected val errorHandler: RetrofitRxErrorHandler) : RxResourceLiveData<T>() {

  init {
    errorHandler.postMessage = ::postErrorRemote
  }

  private var lastRefreshed = 0L
  private val refreshDelayMillis = TimeUnit.MINUTES.toMillis(10)

  protected open fun shouldRefresh() = System.currentTimeMillis() - lastRefreshed > refreshDelayMillis || value == null

  protected open fun clearOnInactive() = true

  fun resetRefreshFlags() {
    lastRefreshed = 0L
  }

  @CallSuper
  override fun onActive() {
    super.onActive()
    if (shouldRefresh()) refresh()
  }

  @CallSuper
  override fun refresh() {
    postLoading()
    lastRefreshed = System.currentTimeMillis()
    clearDisposables()
  }

  fun forceRefresh() {
    resetRefreshFlags()
    refresh()
  }

  @CallSuper
  open fun updateRemote() {
    postLoading()
    lastRefreshed = System.currentTimeMillis()
    clearDisposables()
  }


  override fun setValue(value: T) {
    super.setValue(value)
    postComplete()
  }

  override fun postValue(value: T) {
    super.postValue(value)
    postComplete()
  }

  @CallSuper
  override fun onInactive() {
    super.onInactive()
    if (clearOnInactive()) clearDisposables()
  }
}