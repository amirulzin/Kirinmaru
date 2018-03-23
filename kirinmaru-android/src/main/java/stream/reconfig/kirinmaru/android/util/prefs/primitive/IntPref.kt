package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

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