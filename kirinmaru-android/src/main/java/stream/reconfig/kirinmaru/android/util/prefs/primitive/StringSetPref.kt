package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

/**
 *
 */
open class StringSetPref(key: String, default: Set<String>, prefs: SharedPreferences) : PrefModel<Set<String>>(
    key,
    default,
    { _, _ -> getStringSet(key, default) },
    { _, _ -> putStringSet(key, default) },
    prefs
)