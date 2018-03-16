package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.vo.Novel

/**
 * Novel DAO. New insert will simply replace old ones
 */
@Dao
interface NovelDao : StandardDao<Novel> {
  @Query("SELECT * FROM Novel WHERE origin = :origin")
  fun novelsAsync(origin: String): Flowable<List<Novel>>

  @Query("SELECT * FROM Novel WHERE origin IN (:origins) AND url IN (:urls) ")
  fun novelsAsync(origins: Set<String>, urls: Set<String>): Flowable<List<Novel>>

  @Query("SELECT * FROM Novel WHERE origin IN (:origins) AND url IN (:urls) ")
  fun novels(origins: Set<String>, urls: Set<String>): List<Novel>

  @Query("SELECT * FROM Novel")
  fun allNovelsAsync(): Single<List<Novel>>

  @Query("DELETE FROM Novel")
  fun deleteAll()
}
