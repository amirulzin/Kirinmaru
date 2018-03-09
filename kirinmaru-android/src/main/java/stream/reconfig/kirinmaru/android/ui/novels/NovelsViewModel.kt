package stream.reconfig.kirinmaru.android.ui.novels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import javax.inject.Inject

/**
 *
 */
class NovelsViewModel @Inject constructor(
    application: Application,
    val novels: NovelsLiveData
) : AndroidViewModel(application)