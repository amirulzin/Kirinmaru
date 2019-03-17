package commons.android.core.prefs.primitive

import android.content.SharedPreferences
import commons.android.core.prefs.PrefModel

/**
 *
 */
open class IntPref(
  key: String,
  default: Int,
  prefs: SharedPreferences
) : PrefModel<Int>(
  key = key,
  default = default,
  retrieve = { _, _ -> getInt(key, default) },
  store = { _, data -> putInt(key, data) },
  prefs = prefs
)