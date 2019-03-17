package commons.android.core.prefs

import android.annotation.SuppressLint
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
  private val default: T? = null,
  private inline val retrieve: SharedPreferences.(key: String, default: T?) -> T?,
  private inline val store: SharedPreferences.Editor.(key: String, data: T) -> Unit,
  private val prefs: SharedPreferences
) {

  @WorkerThread
  open fun load(defaultVal: T? = default): T? = retrieve(prefs, key, defaultVal)

  @SuppressLint("ApplySharedPref")
  @AnyThread
  open fun persist(data: T, commit: Boolean = false) {
    prefs.edit()
      .also { store(it, key, data) }
      .run { if (commit) commit() else apply() }
  }

  @SuppressLint("ApplySharedPref")
  @AnyThread
  open fun delete(commit: Boolean = false) {
    prefs.edit()
      .also { it.remove(key) }
      .run { if (commit) commit() else apply() }
  }

  @WorkerThread
  fun safeLoad(): T = load() ?: throw IllegalStateException("$key not assigned")
}