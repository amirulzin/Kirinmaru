package stream.reconfig.kirinmaru.android.ui.favorites

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import stream.reconfig.kirinmaru.android.util.prefs.GsonPrefModel
import javax.inject.Inject

/**
 * Store set NovelItem favorites as Gson string
 */
class FavoritePref @Inject constructor(prefs: SharedPreferences, gson: Gson) : GsonPrefModel<MutableSet<FavoriteNovel>>(
    key = "favorite",
    default = mutableSetOf(),
    prefs = prefs,
    gson = gson,
    type = lazy { object : TypeToken<MutableSet<FavoriteNovel>>() {}.type }
)

