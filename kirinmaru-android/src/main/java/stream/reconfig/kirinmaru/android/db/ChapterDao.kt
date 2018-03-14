package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.core.ChapterId

/**
 * Chapter DAO. New inserts will simply replace old ones
 */
@Dao
interface ChapterDao : StandardDao<Chapter> {

  @Query("SELECT url FROM Chapter WHERE novelUrl = :novelUrl")
  fun chaptersBy(novelUrl: String): Flowable<List<String>>

  @Query("SELECT * FROM Chapter WHERE url = :chapterUrl")
  fun chapterBy(chapterUrl: String): Flowable<Chapter>

  /**
   * Used during [ChapterId] insertion. Conflicts are ignored since there's no
   * need to override chapters with downloaded rawText
   */
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insertLatest(data: List<Chapter>): List<Long>
}