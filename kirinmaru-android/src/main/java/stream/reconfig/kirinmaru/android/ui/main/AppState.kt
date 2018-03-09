package stream.reconfig.kirinmaru.android.ui.main

import stream.reconfig.kirinmaru.android.prefs.FirstLoadPref
import stream.reconfig.kirinmaru.android.prefs.FirstNavPref
import stream.reconfig.kirinmaru.android.prefs.FirstOriginPref
import stream.reconfig.kirinmaru.android.util.validator.ThreadValidator
import javax.inject.Inject

class AppState @Inject constructor(
    private val firstLoadPref: FirstLoadPref,
    private val firstOriginPref: FirstOriginPref,
    private val firstNavPref: FirstNavPref
) {

  val isFirstLoad by lazy {
    firstLoadPref.load(true)
        .apply { if (!this) firstLoadPref.persist(true) }
  }

  val firstNav by lazy { firstNavPref.load() }

  val firstOrigin by lazy { firstOriginPref.load() }

  fun initStates() {
    ThreadValidator.validateWorkerThread()
    isFirstLoad
    firstNav
    firstOrigin
  }
}