package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.util.prefs.primitive.IntPref
import javax.inject.Inject

/**
 * Int based pref to determine which navigation id is first launched.
 *
 * Note: Although related, this is different from the boolean based first_nav_key in Settings.
 */
class FirstNavPref @Inject constructor(prefs: SharedPreferences) : IntPref("firstNav", R.id.navCatalogues, prefs)