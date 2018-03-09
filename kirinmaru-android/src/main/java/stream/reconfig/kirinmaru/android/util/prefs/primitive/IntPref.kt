package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

/**
 *
 */
open class IntPref(key: String, default: Int, prefs: SharedPreferences) : PrefModel<Int>(
    key,
    default,
    { _, _ -> getInt(key, default) },
    { _, _ -> putInt(key, default) },
    prefs
)