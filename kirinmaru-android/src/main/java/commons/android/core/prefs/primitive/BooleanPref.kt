package commons.android.core.prefs.primitive

import android.content.SharedPreferences
import commons.android.core.prefs.PrefModel

/**
 *
 */
open class BooleanPref(
  key: String,
  default: Boolean,
  prefs: SharedPreferences
) : PrefModel<Boolean>(
  key = key,
  default = default,
  retrieve = { _, _ -> getBoolean(key, default) },
  store = { _, data -> putBoolean(key, data) },
  prefs = prefs
)