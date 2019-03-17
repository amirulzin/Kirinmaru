package commons.android.core.search

import android.arch.lifecycle.MutableLiveData

inline fun <T, L : MutableLiveData<List<T>>> L.asFilterableData(crossinline filterBlock: (input: CharSequence, data: T) -> Boolean): FilterableData<T> {
  return object : FilterableData<T>(this) {
    override fun filter(input: CharSequence, data: T): Boolean = filterBlock(input, data)
  }
}

@JvmSuppressWildcards(suppress = false)
abstract class FilterableData<T>(private val liveData: MutableLiveData<List<T>>) {

  private lateinit var originalCopy: List<T>

  abstract fun filter(input: CharSequence, data: T): Boolean

  fun initCopy(list: List<T>) {
    originalCopy = list
  }

  fun applyFilter(input: CharSequence) {
    liveData.postValue(originalCopy.filter { data ->
      filter(input, data)
    })
  }

  fun revertFilter() {
    liveData.postValue(originalCopy)
  }
}