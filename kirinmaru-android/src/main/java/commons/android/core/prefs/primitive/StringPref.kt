package commons.android.core.prefs.primitive

import android.content.SharedPreferences
import commons.android.core.prefs.PrefModel

/**
 *
 */
open class StringPref(
  key: String,
  default: String?,
  prefs: SharedPreferences
) : PrefModel<String?>(
  key = key,
  default = default,
  retrieve = { _, _ -> getString(key, default) },
  store = { _, data -> putString(key, data) },
  prefs = prefs
)