package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

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