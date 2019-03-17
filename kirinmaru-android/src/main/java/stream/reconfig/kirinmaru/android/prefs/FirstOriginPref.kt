package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import commons.android.core.prefs.primitive.StringPref
import stream.reconfig.kirinmaru.plugins.PluginMap
import javax.inject.Inject

/**
 *
 */
class FirstOriginPref @Inject constructor(prefs: SharedPreferences, pluginMap: PluginMap) : StringPref("firstOrigin", pluginMap.keys.first(), prefs)