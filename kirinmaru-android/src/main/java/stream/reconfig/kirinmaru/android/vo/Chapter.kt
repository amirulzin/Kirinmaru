package stream.reconfig.kirinmaru.android.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import stream.reconfig.kirinmaru.core.ChapterDetail
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.NovelId

@Entity(foreignKeys = [
  (ForeignKey(
      entity = Novel::class,
      parentColumns = ["origin", "url"],
      childColumns = ["origin", "novelUrl"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE
  ))]
)
data class Chapter(
    val origin: String,
    val novelUrl: String,
    @PrimaryKey override val url: String,
    override val rawText: String? = null,
    override val nextUrl: String? = null,
    override val previousUrl: String? = null
) : ChapterId, ChapterDetail {

  constructor(novelId: NovelId, chapterId: ChapterId) : this(
      novelId.origin,
      novelId.url,
      chapterId.url
  )

  constructor(novelId: NovelId, chapterId: ChapterId, chapterDetail: ChapterDetail) : this(
      novelId.origin,
      novelId.url,
      chapterId.url,
      chapterDetail.rawText,
      chapterDetail.nextUrl,
      chapterDetail.previousUrl
  )
}