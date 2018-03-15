package stream.reconfig.kirinmaru.android.parcel

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import stream.reconfig.kirinmaru.core.NovelDetail

/**
 * NovelParcel for [NovelDetail] transport
 */
@SuppressLint("ParcelCreator")
data class NovelParcel(
    override val origin: String,
    override val url: String,
    override val novelTitle: String,
    override val id: String?,
    override val tags: Set<String>
) : NovelDetail, AutoParcelable

fun NovelDetail.toParcel() = NovelParcel(origin, url, novelTitle, id, tags)
