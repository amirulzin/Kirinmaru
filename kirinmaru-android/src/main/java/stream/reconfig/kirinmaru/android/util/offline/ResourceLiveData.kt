package stream.reconfig.kirinmaru.android.util.offline

import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import android.support.annotation.CallSuper
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.BuildConfig
import stream.reconfig.kirinmaru.android.util.rx.addTo

/**
 * Standardized **offline-first** LiveData based
 * on the implemented [ResourceContract].
 *
 *   - [V] Type to return as views. e.g, `List<PersonInfo>`, `List<Player>`, `Players`
 *   - [L] Local type to be queried from local store e.g. `List<PersonEntity>`, `PlayerEntities`
 *   - [R] Remote type returned during forced refresh e.g. `List<PersonJson>`, `PlayersJson`
 *
 * Note: Methods of the created [ResourceContract] will be called on RxJava worker pools.
 *
 * Requires
 *   1. RxJava2
 *   2. Android Architecture Components `LiveData` and `ReactiveStreams`
 */
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
abstract class ResourceLiveData<V, L, R> : RxResourceLiveData<V>() {

  protected abstract fun createContract(): ResourceContract<V, L, R>

  protected open val contract by lazy { createContract() }

  override fun refresh() {
    if (resourceState.value?.state != State.LOADING) {
      postLoading()
      contract.remote()
          .map(contract::persist)
          .subscribeOn(Schedulers.io())
          .observeOn(Schedulers.computation())
          .subscribe(
              { success -> postComplete() },
              { error ->
                postError(error?.message ?: "Network error")
                if (BuildConfig.DEBUG) error.printStackTrace()
              }
          ).addTo(disposables)
    }
  }

  @CallSuper
  override fun onActive() {
    super.onActive()
    localLive.observeForever(localObserver)
  }

  @CallSuper
  override fun onInactive() {
    super.onInactive()
    localLive.removeObserver(localObserver)
  }

  protected open val localObserver by lazy {
    Observer<V> { localEmission ->
      localEmission?.run {
        when {
          this is Collection<*> && (this as Collection<*>).isEmpty() -> refresh()
          else -> postValue(this)
        }
      } ?: refresh()

      // While the last refresh() may seems cyclical (and may lead to stack overflow),
      // it is only reachable if localLive emits null. Since that depends on
      // contract.local() emission, local() then should never emits
      // null result. Errors is fine as they are separately consumed via local live.
    }
  }

  protected open val localLive by lazy {
    LiveDataReactiveStreams.fromPublisher(
        contract.local()
            .map(contract::view)
            .doOnError {
              postError(it.message ?: "Error during local retrieval")
              if (BuildConfig.DEBUG) it.printStackTrace()
            }.onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
    )
  }
}