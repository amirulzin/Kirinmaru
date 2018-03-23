package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

/**
 *
 */
open class StringPref(
    key: String,
    default: String,
    prefs: SharedPreferences
) : PrefModel<String>(
    key = key,
    default = default,
    retrieve = { _, _ -> getString(key, default) },
    store = { _, data -> putString(key, data) },
    prefs = prefs
)