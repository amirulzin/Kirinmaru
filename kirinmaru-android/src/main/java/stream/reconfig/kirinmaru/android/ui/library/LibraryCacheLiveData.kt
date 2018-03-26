package stream.reconfig.kirinmaru.android.ui.library

import android.arch.lifecycle.MutableLiveData
import stream.reconfig.kirinmaru.core.NovelId

/**
 * Simple key-value cache for collecting LibraryItem emission and
 * posting each item updates (or delta state) as a cumulative collection update
 */
class LibraryCacheLiveData : MutableLiveData<MutableMap<NovelId, LibraryItem>>() {
  init {
    value = mutableMapOf()
  }

  fun update(libraryItem: LibraryItem) {
    postValue(value().apply { put(libraryItem.novel, libraryItem) })
  }

  fun remove(libraryItem: LibraryItem) {
    postValue(value().apply { remove(libraryItem.novel) })
  }

  private fun value() = value!!
}