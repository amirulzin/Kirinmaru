package stream.reconfig.kirinmaru.android.ui.reader

import android.content.SharedPreferences
import com.google.gson.Gson
import stream.reconfig.kirinmaru.android.util.prefs.GsonPrefModel
import javax.inject.Inject

class ReaderPref @Inject constructor(
    prefs: SharedPreferences,
    gson: Gson
) : GsonPrefModel<ReaderSetting>(
    key = "reader",
    default = ReaderSetting(),
    prefs = prefs,
    gson = gson
)