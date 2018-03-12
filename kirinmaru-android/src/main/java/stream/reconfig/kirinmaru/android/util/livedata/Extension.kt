package stream.reconfig.kirinmaru.android.util.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

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