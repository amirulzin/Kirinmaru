package stream.reconfig.kirinmaru.android.parcel

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.core.ChapterId

/**
 *
 */
@SuppressLint("ParcelCreator")
data class ChapterIdParcel(override val url: String) : ChapterId, AutoParcelable

fun ChapterId.toParcel() = ChapterIdParcel(url)
