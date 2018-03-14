package stream.reconfig.kirinmaru.android.ui.novels

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.core.NovelDetail

/**
 * UI model for Novel items
 */
@SuppressLint("ParcelCreator")
data class NovelItem(
    override val id: String?,
    override val novelTitle: String,
    override val url: String,
    override val tags: Set<String>,
    override val origin: String,
    var isFavorite: Boolean
) : NovelDetail, AutoParcelable

