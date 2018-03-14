package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.util.prefs.PrefDynamicModel
import stream.reconfig.kirinmaru.core.NovelId
import javax.inject.Inject

/**
 * Current read chapter url dynamically keyed by a [NovelId]
 */
class CurrentReadPref @Inject constructor(prefs: SharedPreferences) : PrefDynamicModel<String?, NovelId>(
    default = null,
    retrieve = { key, default -> getString(key, default) },
    store = { key, data -> putString(key, data) },
    prefs = prefs
) {
  override fun key(input: NovelId): String {
    return "cr_${input.origin}_${input.url}"
  }
}