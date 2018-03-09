package stream.reconfig.kirinmaru.android.assets

import android.content.Context
import android.graphics.Typeface
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.di.qualifiers.ApplicationContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

/**
 * Handles retrieval and enumerating fonts as provided under `src/main/asset` folder.
 *
 * Intended to be used as a single instance per application
 */
class Fonts @Inject constructor(@ApplicationContext private val application: Context) {

  private val list by lazy { application.assets.list("fonts") }
  private val map by lazy { ConcurrentHashMap<String, Typeface>(8, 1f) }

  fun asyncList() = Single.just(list)!!

  fun toTypeface(fontPath: String = ""): Typeface =
      if (fontPath.isEmpty() || map[fontPath] == null) Typeface.DEFAULT
      else map[fontPath]!!
}