package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update

/**
 * Standard Room DAO for insert and delete
 */
interface StandardDao<in T> {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertReplace(data: T): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertReplace(data: List<T>): List<Long>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insertIgnore(data: T): Long

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insertIgnore(data: List<T>): List<Long>

  @Update(onConflict = OnConflictStrategy.IGNORE)
  fun updateIgnore(data: T): Int

  @Update(onConflict = OnConflictStrategy.IGNORE)
  fun updateIgnore(data: List<T>): Int

  @Delete
  fun delete(data: T)

  @Delete
  fun delete(data: List<T>)
}