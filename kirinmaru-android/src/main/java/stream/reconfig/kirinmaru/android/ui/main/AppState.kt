package stream.reconfig.kirinmaru.android.ui.main

import commons.android.core.validator.ThreadValidator
import stream.reconfig.kirinmaru.android.prefs.FirstLoadPref
import stream.reconfig.kirinmaru.android.prefs.FirstNavPref
import stream.reconfig.kirinmaru.android.prefs.FirstOriginPref
import javax.inject.Inject

class AppState @Inject constructor(
  private val firstLoadPref: FirstLoadPref,
  private val firstOriginPref: FirstOriginPref,
  private val firstNavPref: FirstNavPref
) {

  val isFirstLoad by lazy {
    with(firstLoadPref) {
      load(true).apply { if (this == true) persist(false) }
    }
  }

  val firstNav by lazy { firstNavPref.loadNonNull() }

  val firstOrigin by lazy { firstOriginPref.loadNonNull() }

  fun initStates() {
    ThreadValidator.validateWorkerThread()
    isFirstLoad
    firstNav
    firstOrigin
  }
}