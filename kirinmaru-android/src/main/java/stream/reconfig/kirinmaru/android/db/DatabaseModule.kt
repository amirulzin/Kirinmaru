package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import stream.reconfig.kirinmaru.android.di.qualifiers.ApplicationContext

/**
 *
 */
@Module
class DatabaseModule {
  @Provides
  fun provideDatabase(@ApplicationContext application: Context): Database {
    return Room.databaseBuilder(application, Database::class.java, "sqlite_db")
        .build()
  }

  @Provides
  fun novelDao(database: Database): NovelDao = database.novelDao()

  @Provides
  fun chapterDao(database: Database): ChapterDao = database.chapterDao()
}