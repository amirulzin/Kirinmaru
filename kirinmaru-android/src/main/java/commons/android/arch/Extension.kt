package commons.android.arch

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.util.Log
import android.widget.Toast
import commons.android.BuildConfigAlias
import commons.android.ProjectConstants.DEBUG_TAG
import commons.android.arch.offline.ResourceState
import commons.android.arch.offline.State

/**
 * Aliased [LiveData.observe] extension for Kotlin
 */
inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T?) -> Unit) {
  this.observe(owner, Observer<T> { t -> observer(t) })
}

/**
 * [LiveData.observe] extension for Kotlin where the
 * passed [observer] only receive non-null emissions from a LiveData
 */
inline fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
  this.observe(owner, Observer<T> { t -> t?.run(observer) })
}

fun <T> AutoRemoteLiveData<T>.observeTerminalStateAsToast(owner: LifecycleOwner, context: Context?) {
  resourceState.observeNonNull(owner) { rs ->
    if (BuildConfigAlias.DEBUG) Log.d(DEBUG_TAG, "Terminal : $rs")
    context?.takeIf { rs.message.isNotBlank() }?.let { ctx ->
      if (rs.state == State.COMPLETE || rs.state == State.ERROR)
        Toast.makeText(ctx, rs.message, Toast.LENGTH_SHORT).show()
    }
  }
}

fun <T> AutoRemoteLiveData<T>.showToastForError(context: Context?, resourceState: ResourceState) {
  if (resourceState.state == State.ERROR) {

    if (BuildConfigAlias.DEBUG)
      Log.e(DEBUG_TAG, "Terminal : $resourceState")

    context?.takeIf { resourceState.message.isNotBlank() }?.let { ctx ->
      Toast.makeText(ctx, resourceState.message, Toast.LENGTH_SHORT).show()
    }
  }
}