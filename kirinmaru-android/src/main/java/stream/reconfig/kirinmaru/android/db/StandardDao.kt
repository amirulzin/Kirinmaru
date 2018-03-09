package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

/**
 * Standard Room DAO for insert and delete
 */
interface StandardDao<in T> {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(data: T): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(data: List<T>): List<Long>

  @Delete
  fun delete(data: T)

  @Delete
  fun delete(data: List<T>)
}