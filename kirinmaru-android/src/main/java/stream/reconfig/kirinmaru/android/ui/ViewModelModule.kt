package stream.reconfig.kirinmaru.android.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import commons.android.arch.ViewModelFactory
import commons.android.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import stream.reconfig.kirinmaru.android.ui.chapters.ChaptersViewModel
import stream.reconfig.kirinmaru.android.ui.library.LibraryViewModel
import stream.reconfig.kirinmaru.android.ui.main.MainViewModel
import stream.reconfig.kirinmaru.android.ui.novels.NovelsViewModel
import stream.reconfig.kirinmaru.android.ui.reader.ReaderViewModel

@Module
interface ViewModelModule {
  @Binds
  fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(MainViewModel::class)
  fun bindMainModel(mvm: MainViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(NovelsViewModel::class)
  fun bindNovels(nvm: NovelsViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(ChaptersViewModel::class)
  fun bindChapters(cvm: ChaptersViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(ReaderViewModel::class)
  fun bindReader(rvm: ReaderViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(LibraryViewModel::class)
  fun bindLibrary(lvm: LibraryViewModel): ViewModel
}