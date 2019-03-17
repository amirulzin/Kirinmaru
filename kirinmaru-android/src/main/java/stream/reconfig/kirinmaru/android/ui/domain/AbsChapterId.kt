package stream.reconfig.kirinmaru.android.ui.domain

import commons.android.core.validator.ThreadValidator
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.taxonomy.Taxonomy

abstract class AbsChapterId : ChapterId {

  val taxonomicNumber: String by lazy {
    ThreadValidator.validateWorkerThread()
    Taxonomy.createTaxonomicNumber(url).also {
      taxonView = Taxonomy.createTaxonomicDisplay(it)
    }
  }
  @Transient
  lateinit var taxonView: String
    private set
}