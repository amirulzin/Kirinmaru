package stream.reconfig.kirinmaru.android.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import stream.reconfig.kirinmaru.android.Kirinmaru
import stream.reconfig.kirinmaru.android.db.DatabaseBugTest
import stream.reconfig.kirinmaru.android.di.scopes.ApplicationScope

/**
 *
 */
@ApplicationScope
@Component(modules = [
  AndroidSupportInjectionModule::class,
  AppModule::class
])
interface AndroidTestAppComponent : AndroidInjector<Kirinmaru> {
  override fun inject(kirinmaru: Kirinmaru)

  fun injectTest(databaseTest: DatabaseBugTest)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): AndroidTestAppComponent.Builder

    fun build(): AndroidTestAppComponent
  }
}