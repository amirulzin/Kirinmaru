package stream.reconfig.kirinmaru.android.ui.novels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import stream.reconfig.kirinmaru.core.NovelId

/**
 * UI model for Novel items
 */
@Parcelize
data class NovelItem(
    override val id: String?,
    override val novelTitle: String,
    override val url: String,
    override val tags: Set<String>,
    val origin: String,
    var isFavorite: Boolean
) : NovelId, Parcelable

