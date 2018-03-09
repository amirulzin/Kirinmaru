package stream.reconfig.kirinmaru.android.prefs

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import stream.reconfig.kirinmaru.android.di.scopes.ApplicationScope

/**
 *
 */
@Module
class PrefModule {

  @ApplicationScope
  @Provides
  fun sharedPreferences(application: Application): SharedPreferences {
    return application.getSharedPreferences("shared_prefs", MODE_PRIVATE)
  }
}