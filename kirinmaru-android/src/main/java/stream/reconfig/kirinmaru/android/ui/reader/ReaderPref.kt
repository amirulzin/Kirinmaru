package stream.reconfig.kirinmaru.android.ui.reader

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import commons.android.dagger.ApplicationContext
import stream.reconfig.kirinmaru.android.util.prefs.GsonDynamicPrefModel
import stream.reconfig.kirinmaru.core.NovelId
import javax.inject.Inject

class ReaderPref @Inject constructor(
  @ApplicationContext context: Context,
  prefs: SharedPreferences,
  gson: Gson
) : GsonDynamicPrefModel<ReaderSetting, NovelId>(
  default = ReaderSetting.default(context),
  prefs = prefs,
  gson = gson,
  type = lazy { object : TypeToken<ReaderSetting>() {}.type }
) {
  override fun key(input: NovelId): String {
    return "rs_${input.origin}_${input.url}"
  }
}