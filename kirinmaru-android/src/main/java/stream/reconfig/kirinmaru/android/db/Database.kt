package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.android.vo.Novel

/**
 * Database
 */
@Database(entities = [Novel::class, Chapter::class], version = 1)
abstract class Database : RoomDatabase() {
  abstract fun novelDao(): NovelDao
  abstract fun chapterDao(): ChapterDao
}

