package commons.android.arch.offline

import android.arch.lifecycle.MutableLiveData
import commons.android.arch.RxMediatorLiveData

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

  fun postCompleteLocal(message: String = "") {
    resourceState.postValue(ResourceState(State.COMPLETE, message, ResourceType.LOCAL))
  }

  fun postCompleteRemote(message: String = "") {
    resourceState.postValue(ResourceState(State.COMPLETE, message, ResourceType.REMOTE))
  }

  fun postErrorLocal(message: String = "") {
    resourceState.postValue(ResourceState(State.ERROR, message, ResourceType.LOCAL))
  }

  fun postErrorRemote(message: String = "") {
    resourceState.postValue(ResourceState(State.ERROR, message, ResourceType.REMOTE))
  }
}
