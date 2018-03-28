package stream.reconfig.kirinmaru.android.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import stream.reconfig.kirinmaru.android.util.prefs.GsonDynamicPrefModel
import stream.reconfig.kirinmaru.android.vo.DBChapterId
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.NovelId
import javax.inject.Inject

/**
 * Current read chapter url dynamically keyed by a [NovelId]
 */
class CurrentReadPref @Inject constructor(
    prefs: SharedPreferences,
    gson: Gson
) : GsonDynamicPrefModel<ChapterId?, NovelId>(
    default = null,
    prefs = prefs,
    gson = gson,
    type = lazy { object : TypeToken<DBChapterId>() {}.type }
) {
  override fun key(input: NovelId): String {
    return "cr_${input.origin}_${input.url}"
  }
}