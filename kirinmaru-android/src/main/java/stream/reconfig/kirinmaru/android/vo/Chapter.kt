package stream.reconfig.kirinmaru.android.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
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
  ))],
    indices = [(Index(value = ["origin", "url"], unique = false))]
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
      origin = novelId.origin,
      novelUrl = novelId.url,
      url = chapterId.url
  )

  constructor(novelId: NovelId, chapterId: ChapterId, chapterDetail: ChapterDetail) : this(
      origin = novelId.origin,
      novelUrl = novelId.url,
      url = chapterId.url,
      rawText = chapterDetail.rawText,
      nextUrl = chapterDetail.nextUrl,
      previousUrl = chapterDetail.previousUrl
  )
}