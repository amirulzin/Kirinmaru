package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.util.prefs.primitive.IntPref
import javax.inject.Inject

/**
 *
 */
class FirstNavPref @Inject constructor(prefs: SharedPreferences) : IntPref("firstNav", R.id.navCatalogues, prefs)