package stream.reconfig.kirinmaru.core

internal const val INTERNAL_MARKER = "~"

interface SearchKey {
  fun getKey(): String {
    return if (this is Enum<*>) {
      "$INTERNAL_MARKER$name"
    } else
      javaClass.simpleName
  }
}


enum class SearchKeys : SearchKey {
  PAGE,
  TERM,
  ALL,
  PAGED_LIST;

  enum class Direction : SearchKey {
    NEXT,
    PREVIOUS
  }
}