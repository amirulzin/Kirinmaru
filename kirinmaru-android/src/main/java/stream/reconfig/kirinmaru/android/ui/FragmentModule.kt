package stream.reconfig.kirinmaru.android.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import stream.reconfig.kirinmaru.android.ui.chapters.ChaptersFragment
import stream.reconfig.kirinmaru.android.ui.novels.NovelsFragment

/**
 *
 */
@Module
interface FragmentModule {

  @ContributesAndroidInjector
  fun contributeNovelsFragment(): NovelsFragment

  @ContributesAndroidInjector
  fun contributeChaptersFragment(): ChaptersFragment
}