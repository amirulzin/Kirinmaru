package stream.reconfig.kirinmaru.android.ui.chapters

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import javax.inject.Inject

/**
 * Chapters view model
 */
class ChaptersViewModel @Inject constructor(
    application: Application,
    val chapters: ChaptersLiveData
) : AndroidViewModel(application)

