package stream.reconfig.kirinmaru.android.ui.library

import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class LibraryViewModel @Inject constructor(val library: LibraryLiveData) : ViewModel()