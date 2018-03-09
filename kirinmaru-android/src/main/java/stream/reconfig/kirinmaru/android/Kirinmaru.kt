package stream.reconfig.kirinmaru.android

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import stream.reconfig.kirinmaru.android.di.DaggerAppComponent

/**
 * Kirinmaru base Application class
 */
class Kirinmaru : DaggerApplication() {
  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.builder()
        .application(this)
        .build()
  }
}