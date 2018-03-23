package stream.reconfig.kirinmaru.android.ui.reader

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import stream.reconfig.kirinmaru.android.di.qualifiers.ApplicationContext
import stream.reconfig.kirinmaru.android.util.prefs.GsonPrefModel
import javax.inject.Inject

class ReaderPref @Inject constructor(
    @ApplicationContext context: Context,
    prefs: SharedPreferences,
    gson: Gson
) : GsonPrefModel<ReaderSetting>(
    key = "reader",
    default = ReaderSetting.default(context),
    prefs = prefs,
    gson = gson,
    type = lazy { object : TypeToken<ReaderSetting>() {}.type }
)