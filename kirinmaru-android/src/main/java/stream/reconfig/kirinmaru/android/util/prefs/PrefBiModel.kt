package stream.reconfig.kirinmaru.android.util.prefs

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Preference model of a given [T] type to its serialized type [S].
 *
 * [S] must be any valid SharedPreferences primitive type (String set included)
 */
open class PrefBiModel<in T, S>(
    private val key: String,
    private val default: S,
    private inline val retrieve: SharedPreferences.(key: String, default: S) -> S,
    private inline val store: SharedPreferences.Editor.(key: String, data: T) -> Unit,
    private val prefs: SharedPreferences
) {
  open fun load(default: S): Single<S> = Single.fromCallable { retrieve(prefs, key, default) }

  open fun persist(data: T): Completable = Completable.fromCallable {
    prefs.edit().also { store(it, key, data) }.apply()
  }
}