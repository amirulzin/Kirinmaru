package stream.reconfig.kirinmaru.core

typealias SearchOptions = Map<SearchKey, String>

typealias MutableSearchOptions = MutableMap<SearchKey, String>

fun SearchOptions.getTerm() = get(SearchKeys.TERM)

fun SearchOptions.getSearchPage() = get(SearchKeys.PAGE)?.toInt()

object SearchOptionsBuilder {
  @JvmStatic
  fun new(): MutableSearchOptions = mutableMapOf()
}

fun MutableSearchOptions.setTerm(searchTerm: String) = apply {
  this[SearchKeys.TERM] = searchTerm
}

fun SearchOptions.getSearchPageOrDefault(page: Int): Int {
  return get(SearchKeys.PAGE)?.toInt() ?: page
}

fun SearchOptions.isPaged() = containsKey(SearchKeys.PAGED_LIST)