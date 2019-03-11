package stream.reconfig.kirinmaru.android.parcel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import stream.reconfig.kirinmaru.core.ChapterId

/**
 *
 */
@Parcelize
data class ChapterIdParcel(override val url: String, override val title: String?) : ChapterId, Parcelable

fun ChapterId.toParcel() = ChapterIdParcel(url, title)
