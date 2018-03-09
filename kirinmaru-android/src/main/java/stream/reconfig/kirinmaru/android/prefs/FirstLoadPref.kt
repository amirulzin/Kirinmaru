package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.primitive.BooleanPref
import javax.inject.Inject

/**
 *
 */
class FirstLoadPref @Inject constructor(prefs: SharedPreferences) : BooleanPref("firstLoad", true, prefs)