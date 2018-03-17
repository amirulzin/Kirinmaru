package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Flowable
import io.reactivex.Single
import stream.reconfig.kirinmaru.android.vo.Novel

/**
 * Novel DAO. New insert will simply replace old ones
 */
@Dao
abstract class NovelDao : UpsertDao<Novel>() {
  @Query("SELECT * FROM Novel WHERE origin = :origin")
  abstract fun novelsAsync(origin: String): Flowable<List<Novel>>

  @Query("SELECT * FROM Novel WHERE origin IN (:origins) AND url IN (:urls)")
  abstract fun novelsAsync(origins: Set<String>, urls: Set<String>): Flowable<List<Novel>>

  @Transaction
  @Query("SELECT * FROM Novel WHERE origin IN (:origins) AND url IN (:urls)")
  abstract fun novels(origins: Set<String>, urls: Set<String>): List<Novel>

  @Query("SELECT * FROM Novel WHERE origin = :origin AND url = :url")
  abstract fun novel(origin: String, url: String): Novel

  @Query("SELECT * FROM Novel")
  abstract fun allNovelsAsync(): Single<List<Novel>>

  @Query("DELETE FROM Novel")
  abstract fun deleteAll()
}
