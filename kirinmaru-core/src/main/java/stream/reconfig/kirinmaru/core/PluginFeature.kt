package stream.reconfig.kirinmaru.core

/**
 * If the keys below doesn't exist when the Plugin is being used, then it default to the feature being unavailable
 */
enum class PluginFeature {
  COVER_IMAGE,
  CAN_SEARCH_NOVEL,
  PAGED_SEARCH,
  PAGED_CHAPTER_IDS,
}