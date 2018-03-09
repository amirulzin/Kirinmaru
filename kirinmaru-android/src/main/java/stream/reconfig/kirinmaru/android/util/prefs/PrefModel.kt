package stream.reconfig.kirinmaru.android.util.prefs

import android.content.SharedPreferences
import android.support.annotation.AnyThread
import android.support.annotation.WorkerThread

/**
 * Preference model of a given [T] type
 *
 * [T] must be any valid SharedPreferences primitive type (String set included)
 */
open class PrefModel<T>(
    private val key: String,
    private val default: T,
    private inline val retrieve: SharedPreferences.(key: String, default: T) -> T,
    private inline val store: SharedPreferences.Editor.(key: String, data: T) -> Unit,
    private val prefs: SharedPreferences
) {

  @WorkerThread
  open fun load(defaultVal: T = default): T = retrieve(prefs, key, defaultVal)

  @AnyThread
  open fun persist(data: T) {
    prefs.edit().also { store(it, key, data) }.apply()
  }
}