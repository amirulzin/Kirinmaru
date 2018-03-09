package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefModel
import javax.inject.Inject

/**
 *
 */
class FavoritePref @Inject constructor(prefs: SharedPreferences) : PrefModel<MutableSet<String>>(
    key = "favorite",
    default = mutableSetOf(),
    retrieve = { key, default -> getStringSet(key, default).toMutableSet() },
    store = { key, data -> putStringSet(key, data) },
    prefs = prefs
)

