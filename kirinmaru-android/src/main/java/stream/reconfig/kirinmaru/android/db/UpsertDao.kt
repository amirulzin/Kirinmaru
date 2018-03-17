package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Transaction

/**
 * Room DAO with Upsert
 */
abstract class UpsertDao<in T> : StandardDao<T> {
  @Transaction
  open fun upsert(data: T) {
    insertIgnore(data)
    updateIgnore(data)
  }

  @Transaction
  open fun upsert(data: List<T>) {
    insertIgnore(data)
    updateIgnore(data)
  }
}