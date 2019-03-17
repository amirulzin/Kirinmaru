package commons.android.core.prefs.primitive

import android.content.SharedPreferences
import commons.android.core.prefs.PrefModel

/**
 *
 */
open class LongPref(
  key: String,
  default: Long,
  prefs: SharedPreferences
) : PrefModel<Long>(
  key = key,
  default = default,
  retrieve = { _, _ -> getLong(key, default) },
  store = { _, data -> putLong(key, data) },
  prefs = prefs
)