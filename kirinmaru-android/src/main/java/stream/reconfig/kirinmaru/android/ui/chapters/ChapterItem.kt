package stream.reconfig.kirinmaru.android.ui.chapters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import stream.reconfig.kirinmaru.core.ChapterId

/**
 * ChapterItem for views
 */
@Parcelize
data class ChapterItem(override val url: String) : ChapterId, Parcelable