package stream.reconfig.kirinmaru.android.util.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    type: Lazy<Type> = lazy { typeToken<T>() }
) : PrefModel<T>(
    key = key,
    default = default,
    retrieve = { key, default ->
      gson.fromJson(getString(key, null), type.value) ?: default
    },
    store = { key, data -> putString(key, gson.toJson(data)) },
    prefs = prefs
) {
  companion object {
    @JvmStatic
    private fun <T> typeToken() = object : TypeToken<T>() {}.type
  }
}



