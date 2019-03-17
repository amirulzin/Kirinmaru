package stream.reconfig.kirinmaru.android.assets

import android.content.Context
import android.graphics.Typeface
import commons.android.dagger.ApplicationContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

/**
 * Handles retrieval and enumerating fonts as provided under `src/main/asset` folder.
 *
 * Intended to be used as a single instance per application
 */
class Fonts @Inject constructor(@ApplicationContext private val application: Context) {
  companion object {
    const val DEFAULT_TYPEFACE_NAME = "Default (Roboto)"
    private const val FONT_FOLDER = "fonts"
  }

  val list by lazy {
    application.assets.list(FONT_FOLDER).let { result ->
      (result ?: emptyArray())
        .filter { it.isNotBlank() }
        .toMutableList()
        .apply { add(DEFAULT_TYPEFACE_NAME) }
        .sorted()
    }
  }

  private val map by lazy { ConcurrentHashMap<String, Typeface>(8, 1f) }

  fun toTypeface(fontPath: String = ""): Typeface {
    var typeface = Typeface.DEFAULT
    if (fontPath == DEFAULT_TYPEFACE_NAME || fontPath.isEmpty())
      return typeface

    if (map[fontPath] == null) {
      map[fontPath] = Typeface.createFromAsset(application.assets, "$FONT_FOLDER/$fontPath")
      typeface = map[fontPath]
    }
    return typeface
  }
}