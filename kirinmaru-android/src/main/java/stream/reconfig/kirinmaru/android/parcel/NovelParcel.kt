package stream.reconfig.kirinmaru.android.parcel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import stream.reconfig.kirinmaru.core.NovelDetail

/**
 * NovelParcel for [NovelDetail] transport
 */
@Parcelize
data class NovelParcel(
  override val origin: String,
  override val url: String,
  override val novelTitle: String,
  override val id: String?,
  override val tags: Set<String>
) : NovelDetail, Parcelable

fun NovelDetail.toParcel() = NovelParcel(origin, url, novelTitle, id, tags)
