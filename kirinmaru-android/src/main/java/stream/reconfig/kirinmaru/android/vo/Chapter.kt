package stream.reconfig.kirinmaru.android.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import stream.reconfig.kirinmaru.core.ChapterDetail
import stream.reconfig.kirinmaru.core.ChapterId

@Entity(foreignKeys = [
  (ForeignKey(
      entity = Novel::class,
      parentColumns = ["url"],
      childColumns = ["novelUrl"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE
  ))]
)
data class Chapter(
    val novelUrl: String,
    @PrimaryKey override val url: String,
    override val rawText: String? = null,
    override val nextUrl: String? = null,
    override val previousUrl: String? = null
) : ChapterId, ChapterDetail