package commons.android.dagger.compat

import android.support.v4.app.JobIntentService

import dagger.android.AndroidInjection

abstract class DaggerJobIntentService : JobIntentService() {
  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }
}
