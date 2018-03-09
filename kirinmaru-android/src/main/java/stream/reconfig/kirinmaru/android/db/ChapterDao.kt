package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.vo.Chapter

/**
 * Chapter DAO. New inserts will simply replace old ones
 */
@Dao
interface ChapterDao : StandardDao<Chapter> {

  @Query("SELECT url FROM Chapter WHERE novelUrl = :novelUrl")
  fun chaptersBy(novelUrl: String): Flowable<List<String>>

  @Query("SELECT url FROM Chapter WHERE novelUrl = :novelUrl")
  fun chapters(novelUrl: String): List<String>

  @Query("SELECT * FROM Chapter WHERE url = :chapterUrl")
  fun chapterBy(chapterUrl: String): Single<Chapter>
}