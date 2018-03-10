package stream.reconfig.kirinmaru.android.ui.chapters

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.core.ChapterId

/**
 * ChapterItem for views
 */
@SuppressLint("ParcelCreator")
data class ChapterItem(override val url: String, val taxon: String) : ChapterId, AutoParcelable