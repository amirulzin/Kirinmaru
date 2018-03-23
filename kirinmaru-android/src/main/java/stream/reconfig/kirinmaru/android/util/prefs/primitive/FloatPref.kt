package stream.reconfig.kirinmaru.android.util.prefs.primitive

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel

/**
 *
 */
open class FloatPref(
    key: String,
    default: Float,
    prefs: SharedPreferences
) : PrefModel<Float>(
    key = key,
    default = default,
    retrieve = { _, _ -> getFloat(key, default) },
    store = { _, data -> putFloat(key, data) },
    prefs = prefs
)