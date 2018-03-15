package stream.reconfig.kirinmaru.android.ui.reader

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.android.parcel.ChapterIdParcel
import stream.reconfig.kirinmaru.android.parcel.NovelParcel

@SuppressLint("ParcelCreator")
data class ReaderData(
    val novelParcel: NovelParcel,
    val chapterParcel: ChapterIdParcel
) : AutoParcelable