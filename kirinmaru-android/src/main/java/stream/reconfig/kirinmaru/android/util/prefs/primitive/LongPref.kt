package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

/**
 *
 */
open class LongPref(key: String, default: Long, prefs: SharedPreferences) : PrefModel<Long>(
    key,
    default,
    { _, _ -> getLong(key, default) },
    { _, _ -> putLong(key, default) },
    prefs
)