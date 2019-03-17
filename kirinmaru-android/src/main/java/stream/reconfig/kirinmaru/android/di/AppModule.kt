package stream.reconfig.kirinmaru.android.di

import android.app.Application
import android.content.Context
import commons.android.dagger.ApplicationContext
import dagger.Binds
import dagger.Module
import stream.reconfig.kirinmaru.android.db.DatabaseModule
import stream.reconfig.kirinmaru.android.network.NetworkModule
import stream.reconfig.kirinmaru.android.prefs.PrefModule
import stream.reconfig.kirinmaru.android.ui.ActivityModule
import stream.reconfig.kirinmaru.android.ui.FragmentModule
import stream.reconfig.kirinmaru.android.ui.ViewModelModule
import stream.reconfig.kirinmaru.plugins.PluginModule


@Module(includes = [
  NetworkModule::class,
  PluginModule::class,
  PrefModule::class,
  DatabaseModule::class,
  ActivityModule::class,
  FragmentModule::class,
  ViewModelModule::class
])
interface AppModule {
  @Binds
  @ApplicationContext
  fun bindContext(application: Application): Context
}

