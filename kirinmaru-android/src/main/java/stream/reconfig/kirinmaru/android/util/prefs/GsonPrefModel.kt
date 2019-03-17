package stream.reconfig.kirinmaru.android.util.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import commons.android.core.prefs.PrefModel
import java.lang.reflect.Type

/**
 * PrefModel based on a Gson-friendly type [T].
 *
 * Data is stored as JSON string.
 *
 * If [T] is nullable, set the `default` constructor parameter to null
 */
open class GsonPrefModel<T>(
  key: String,
  default: T,
  gson: Gson,
  prefs: SharedPreferences,
  type: Lazy<Type>
) : PrefModel<T>(
  key = key,
  default = default,
  retrieve = { key, default ->
    gson.fromJson(getString(key, null), type.value) ?: default
  },
  store = { key, data -> putString(key, gson.toJson(data)) },
  prefs = prefs
)



