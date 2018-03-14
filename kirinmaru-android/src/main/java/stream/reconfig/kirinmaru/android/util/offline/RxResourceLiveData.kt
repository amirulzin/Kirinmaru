package stream.reconfig.kirinmaru.android.util.offline

import android.arch.lifecycle.MutableLiveData
import stream.reconfig.kirinmaru.android.util.livedata.RxMediatorLiveData

/**
 * Rx resource-based live data model
 */
abstract class RxResourceLiveData<T> : RxMediatorLiveData<T>() {

  open val resourceState = MutableLiveData<ResourceState>()

  abstract fun refresh()

  fun postState(state: ResourceState?) {
    resourceState.postValue(state)
  }

  fun postError(message: String = "", type: ResourceType = ResourceType.ANY) {
    resourceState.postValue(ResourceState(State.ERROR, message, type))
  }

  fun postLoading(message: String = "", type: ResourceType = ResourceType.ANY) {
    resourceState.postValue(ResourceState(State.LOADING, message, type))
  }

  fun postComplete(message: String = "", type: ResourceType = ResourceType.ANY) {
    resourceState.postValue(ResourceState(State.COMPLETE, message, type))
  }
}
