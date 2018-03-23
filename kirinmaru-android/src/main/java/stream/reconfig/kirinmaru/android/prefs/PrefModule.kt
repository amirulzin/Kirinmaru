package stream.reconfig.kirinmaru.android.prefs

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import stream.reconfig.kirinmaru.android.di.scopes.ApplicationScope

internal const val SHARED_PREF_NAME = "shared_prefs"

/**
 *
 */
@Module
class PrefModule {

  @ApplicationScope
  @Provides
  fun sharedPreferences(application: Application): SharedPreferences {
    return application.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
  }
}