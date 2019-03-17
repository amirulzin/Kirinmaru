package commons.android.core.prefs.primitive

import android.content.SharedPreferences
import commons.android.core.prefs.PrefModel

/**
 *
 */
open class StringSetPref(
  key: String,
  default: Set<String>,
  prefs: SharedPreferences
) : PrefModel<Set<String>>(
  key = key,
  default = default,
  retrieve = { _, _ -> getStringSet(key, default) },
  store = { _, data -> putStringSet(key, data) },
  prefs = prefs
)