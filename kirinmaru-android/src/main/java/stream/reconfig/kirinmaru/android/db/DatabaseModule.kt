package stream.reconfig.kirinmaru.android.db

import android.arch.persistence.room.Room
import android.content.Context
import commons.android.dagger.ApplicationContext
import commons.android.dagger.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 *
 */
@Module
class DatabaseModule {
  @ApplicationScope
  @Provides
  fun provideDatabase(@ApplicationContext application: Context): Database {
    return Room.databaseBuilder(application, Database::class.java, "sqlite.db")
        .build()
  }

  @Provides
  fun novelDao(database: Database): NovelDao = database.novelDao()

  @Provides
  fun chapterDao(database: Database): ChapterDao = database.chapterDao()
}