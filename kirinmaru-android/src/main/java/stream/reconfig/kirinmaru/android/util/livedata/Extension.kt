package stream.reconfig.kirinmaru.android.util.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

/**
 * Aliased [LiveData.observe] extension for Kotlin
 */
fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) {
  this.observe(owner, Observer(observer))
}