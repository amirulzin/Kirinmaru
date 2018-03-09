package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

/**
 *
 */
open class FloatPref(key: String, default: Float, prefs: SharedPreferences) : PrefModel<Float>(
    key,
    default,
    { _, _ -> getFloat(key, default) },
    { _, _ -> putFloat(key, default) },
    prefs
)