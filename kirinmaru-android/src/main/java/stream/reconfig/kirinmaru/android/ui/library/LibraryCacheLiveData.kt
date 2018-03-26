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

  fun update(list: List<LibraryItem>) {
    list.map { value().put(it.novel, it) }
    postValue(value())
  }

  fun remove(novelId: NovelId) {
    postValue(value().apply { remove(novelId) })
  }

  private fun value() = value!!
}