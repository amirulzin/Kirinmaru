package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

/**
 *
 */
open class StringPref(key: String, default: String, prefs: SharedPreferences) : PrefModel<String>(
    key,
    default,
    { _, _ -> getString(key, default) },
    { _, _ -> putString(key, default) },
    prefs
)