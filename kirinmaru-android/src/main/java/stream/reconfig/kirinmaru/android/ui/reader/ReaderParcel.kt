package stream.reconfig.kirinmaru.android.ui.reader

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import stream.reconfig.kirinmaru.android.parcel.ChapterIdParcel
import stream.reconfig.kirinmaru.android.parcel.NovelParcel

@Parcelize
data class ReaderParcel(
  val novelParcel: NovelParcel,
  val chapterParcel: ChapterIdParcel
) : Parcelable