package stream.reconfig.kirinmaru.android.di

import android.app.Application
import commons.android.dagger.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import stream.reconfig.kirinmaru.android.Kirinmaru

@ApplicationScope
@Component(modules = [
  AndroidSupportInjectionModule::class,
  AppModule::class
])
interface AppComponent : AndroidInjector<Kirinmaru> {
  override fun inject(kirinmaru: Kirinmaru)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): AppComponent.Builder

    fun build(): AppComponent
  }
}