package stream.reconfig.kirinmaru.android.ui.reader

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import stream.reconfig.kirinmaru.android.di.qualifiers.ApplicationContext
import stream.reconfig.kirinmaru.android.util.prefs.GsonDynamicPrefModel
import stream.reconfig.kirinmaru.core.NovelId
import javax.inject.Inject

class ReaderPref @Inject constructor(
    @ApplicationContext context: Context,
    prefs: SharedPreferences,
    gson: Gson
) : GsonDynamicPrefModel<ReaderSetting, NovelId?>(
    default = ReaderSetting.default(context),
    prefs = prefs,
    gson = gson,
    type = lazy { object : TypeToken<ReaderSetting>() {}.type }
) {
  override fun key(input: NovelId?): String {
    return input?.let { "rs_${it.origin}_${it.url}" } ?: "rs_global"
  }
}