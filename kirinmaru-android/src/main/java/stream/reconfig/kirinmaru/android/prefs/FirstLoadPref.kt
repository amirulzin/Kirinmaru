package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import commons.android.core.prefs.primitive.BooleanPref
import javax.inject.Inject

/**
 *
 */
class FirstLoadPref @Inject constructor(prefs: SharedPreferences) : BooleanPref("firstLoad", true, prefs)