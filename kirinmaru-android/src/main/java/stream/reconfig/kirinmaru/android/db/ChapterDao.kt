package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Flowable
import stream.reconfig.kirinmaru.android.vo.Chapter

/**
 * Chapter DAO. New inserts will simply replace old ones
 */
@Dao
abstract class ChapterDao : UpsertDao<Chapter>() {

  @Transaction
  @Query("SELECT url FROM Chapter WHERE origin = :origin AND novelUrl = :novelUrl")
  abstract fun chapters(origin: String, novelUrl: String): List<String>

  @Query("SELECT url FROM Chapter WHERE origin = (:origin) AND novelUrl = (:novelUrl)")
  abstract fun chaptersAsync(origin: String, novelUrl: String): Flowable<List<String>>

  @Query("SELECT * FROM Chapter WHERE url = :chapterUrl")
  abstract fun chapterAsync(chapterUrl: String): Flowable<Chapter>

}