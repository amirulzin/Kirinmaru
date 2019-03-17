package stream.reconfig.kirinmaru.android.ui.novels

import stream.reconfig.kirinmaru.core.NovelDetail

/**
 * UI model for Novel items
 */
data class NovelItem(
  override val id: String?,
  override val novelTitle: String,
  override val url: String,
  override val tags: Set<String>,
  override val origin: String,
  var isFavorite: Boolean
) : NovelDetail

