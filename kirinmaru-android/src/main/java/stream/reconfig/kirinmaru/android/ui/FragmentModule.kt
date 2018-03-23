package stream.reconfig.kirinmaru.android.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import stream.reconfig.kirinmaru.android.ui.chapters.ChaptersFragment
import stream.reconfig.kirinmaru.android.ui.library.LibraryFragment
import stream.reconfig.kirinmaru.android.ui.novels.NovelsFragment
import stream.reconfig.kirinmaru.android.ui.reader.ReaderFragment
import stream.reconfig.kirinmaru.android.ui.settings.PrefChildFragment
import stream.reconfig.kirinmaru.android.ui.settings.SettingsFragment

/**
 *
 */
@Module
interface FragmentModule {

  @ContributesAndroidInjector
  fun contributeNovelsFragment(): NovelsFragment

  @ContributesAndroidInjector
  fun contributeChaptersFragment(): ChaptersFragment

  @ContributesAndroidInjector
  fun contributeReaderFragment(): ReaderFragment

  @ContributesAndroidInjector
  fun contributeLibraryFragment(): LibraryFragment

  @ContributesAndroidInjector
  fun contributeSettingsFragment(): SettingsFragment

  @ContributesAndroidInjector
  fun contributePrefChildFragment(): PrefChildFragment
}