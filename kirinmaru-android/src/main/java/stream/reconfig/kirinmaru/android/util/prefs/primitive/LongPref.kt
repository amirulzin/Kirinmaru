package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

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