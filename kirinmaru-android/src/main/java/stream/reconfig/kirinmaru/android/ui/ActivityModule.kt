package stream.reconfig.kirinmaru.android.ui

import android.content.Context
import commons.android.dagger.ActivityContext
import commons.android.dagger.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import stream.reconfig.kirinmaru.android.ui.main.MainActivity

@Module
interface ActivityModule {

  @ContributesAndroidInjector
  @ActivityScope
  fun contributeActivity(): MainActivity

  @Binds
  @ActivityContext
  @ActivityScope
  fun bindContext(activity: MainActivity): Context
}