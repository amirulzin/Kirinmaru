package commons.android.arch.offline

import android.support.annotation.AnyThread
import android.support.annotation.CallSuper
import commons.android.rx.addTo
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

/**
 * Simplified **offline-first** LiveData based on the implemented [ResourceContract].
 * Invoking [refresh] will glue both remote and local subscription.
 *
 * Except the state checking to avoid multiple refresh running at the same time,
 * each refresh is idempotent from any previous refresh call.
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
abstract class SimpleResourceLiveData<V, L, R> : RxResourceLiveData<V>() {

  protected abstract fun createContract(): ResourceContract<V, L, R>

  protected open val contract by lazy { createContract() }

  protected open fun isInitialized() = resourceState.value != null

  @CallSuper
  override fun onActive() {
    super.onActive()
    if (contract.autoFetch()) refresh()
  }

  @AnyThread
  override fun refresh() {
    if (resourceState.value?.state != State.LOADING) {
      postLoading()

      Completable.fromCallable {
        disposables.clear()

        contract.local()
          .map(contract::view)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .subscribe(
            { success -> postValue(success); postCompleteLocal() },
            { error -> postErrorLocal(error?.message ?: "Database error") },
            { postComplete() }
          ).addTo(disposables)

        contract.remote()
          .map(contract::persist)
          .subscribeOn(Schedulers.io())
          .subscribe(
            { _ -> postCompleteRemote() },
            { error -> postErrorRemote(error?.message ?: "Network error") }
          ).addTo(disposables)
      }.subscribeOn(Schedulers.computation())
        .subscribe()
    }
  }
}