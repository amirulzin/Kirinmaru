package stream.reconfig.kirinmaru.android.util.offline

import android.annotation.SuppressLint
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.util.rx.addTo

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
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
abstract class SimpleResourceLiveData<V, L, R> : RxResourceLiveData<V>() {

  protected abstract fun createContract(): ResourceContract<V, L, R>

  protected open val contract by lazy { createContract() }

  protected open fun isInitialized() = resourceState.value != null

  @SuppressLint("CheckResult")
  override fun refresh() {
    if (resourceState.value?.state != State.LOADING) {
      postLoading()
      disposables.clear()
      contract.remote()
          .map(contract::transform)
          .map(contract::persist)
          .subscribeOn(Schedulers.io())
          .observeOn(Schedulers.computation())
          .subscribe(
              { success -> postComplete() },
              { error -> postError(error?.message ?: "Network error") }
          ).addTo(disposables)

      contract.local()
          .map(contract::view)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .subscribe(
              { success -> postValue(success); postComplete() },
              { error -> postError(error?.message ?: "Network error") },
              { postComplete() }
          ).addTo(disposables)
    }
  }
}