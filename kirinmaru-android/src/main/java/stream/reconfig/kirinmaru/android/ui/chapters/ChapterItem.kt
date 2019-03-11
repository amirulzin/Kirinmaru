package stream.reconfig.kirinmaru.android.ui.chapters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import stream.reconfig.kirinmaru.android.ui.domain.AbsChapterId

/**
 * ChapterItem for views
 */
@Parcelize
data class ChapterItem(
  override val url: String,
  override val title: String?,
  val currentRead: Boolean
) : AbsChapterId(), Parcelable