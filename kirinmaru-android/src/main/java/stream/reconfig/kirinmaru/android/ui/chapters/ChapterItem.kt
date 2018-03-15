package stream.reconfig.kirinmaru.android.ui.chapters

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.android.ui.domain.AbsChapterId

/**
 * ChapterItem for views
 */
@SuppressLint("ParcelCreator")
data class ChapterItem(
    override val url: String,
    val currentRead: Boolean
) : AbsChapterId(), AutoParcelable