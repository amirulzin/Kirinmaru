package stream.reconfig.kirinmaru.android.ui.reader

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.android.ui.chapters.ChapterItem
import stream.reconfig.kirinmaru.android.ui.novels.NovelItem

@SuppressLint("ParcelCreator")
data class ReaderData(
    val novelItem: NovelItem,
    val chapterItem: ChapterItem
) : AutoParcelable