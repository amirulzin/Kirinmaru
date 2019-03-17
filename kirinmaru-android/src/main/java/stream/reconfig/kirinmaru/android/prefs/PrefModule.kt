package stream.reconfig.kirinmaru.android.prefs

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import commons.android.dagger.ApplicationScope
import dagger.Module
import dagger.Provides

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