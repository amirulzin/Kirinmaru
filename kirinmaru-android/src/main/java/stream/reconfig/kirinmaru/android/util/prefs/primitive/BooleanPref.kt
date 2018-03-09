package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

/**
 *
 */
open class BooleanPref(private val key: String, private val default: Boolean, prefs: SharedPreferences) : PrefModel<Boolean>(
    key,
    default,
    { _, _ -> getBoolean(key, default) },
    { _, _ -> putBoolean(key, default) },
    prefs
)