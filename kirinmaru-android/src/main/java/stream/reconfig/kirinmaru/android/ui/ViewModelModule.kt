package stream.reconfig.kirinmaru.android.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import stream.reconfig.kirinmaru.android.di.keys.ViewModelKey
import stream.reconfig.kirinmaru.android.ui.chapters.ChaptersViewModel
import stream.reconfig.kirinmaru.android.ui.main.MainViewModel
import stream.reconfig.kirinmaru.android.ui.novels.NovelsViewModel
import stream.reconfig.kirinmaru.android.ui.reader.ReaderViewModel
import stream.reconfig.kirinmaru.android.util.viewmodel.ViewModelFactory

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
  fun bindReader(cvm: ReaderViewModel): ViewModel
}