package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.primitive.StringPref
import javax.inject.Inject

/**
 *
 */
class CookiePref @Inject constructor(prefs: SharedPreferences) : StringPref("cookie", "", prefs)