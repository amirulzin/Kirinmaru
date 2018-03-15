package stream.reconfig.kirinmaru.android.ui.reader

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.android.parcel.NovelParcel
import stream.reconfig.kirinmaru.android.ui.chapters.ChapterItem

@SuppressLint("ParcelCreator")
data class ReaderData(
    val novelParcel: NovelParcel,
    val chapterItem: ChapterItem
) : AutoParcelable