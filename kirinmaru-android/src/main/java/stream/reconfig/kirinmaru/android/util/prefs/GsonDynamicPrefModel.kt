package stream.reconfig.kirinmaru.android.util.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import commons.android.core.prefs.PrefDynamicModel
import java.lang.reflect.Type

/**
 * PrefDynamicModel based on a Gson-friendly type [T]
 * and String key created from a [P] parameter.
 *
 * Data is stored as JSON string.
 *
 * If [T] is nullable, set the `default` constructor parameter to null.
 */
abstract class GsonDynamicPrefModel<T, in P>(
  default: T,
  gson: Gson,
  prefs: SharedPreferences,
  type: Lazy<Type>
) : PrefDynamicModel<T, P>(
  default = default,
  retrieve = { key, default ->
    gson.fromJson(getString(key, null), type.value) ?: default
  },
  store = { key, data -> putString(key, gson.toJson(data)) },
  prefs = prefs
)



