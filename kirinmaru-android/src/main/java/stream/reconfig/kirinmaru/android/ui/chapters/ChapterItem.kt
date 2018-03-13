package stream.reconfig.kirinmaru.android.ui.chapters

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.taxonomy.Taxonomy

/**
 * ChapterItem for views
 */
@SuppressLint("ParcelCreator")
data class ChapterItem(
    override val url: String
) : ChapterId, AutoParcelable {
  val taxonomicNumber: String  by lazy {
    Taxonomy.createTaxonomicNumber(url).also {
      taxonView = Taxonomy.createTaxonomicDisplay(it)
    }
  }
  lateinit var taxonView: String
    private set
}
