package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import stream.reconfig.kirinmaru.android.ui.novels.NovelItem
import stream.reconfig.kirinmaru.android.util.prefs.PrefDynamicModel
import javax.inject.Inject

/**
 * Current read chapter dynamic keyed by a [NovelItem]
 */
class CurrentReadPref @Inject constructor(prefs: SharedPreferences) : PrefDynamicModel<String?, NovelItem>(
    default = null,
    retrieve = { key, default -> getString(key, default) },
    store = { key, data -> putString(key, data) },
    prefs = prefs
) {
  override fun key(input: NovelItem): String {
    return "curRead_${input.id}"
  }
}