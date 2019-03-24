package stream.reconfig.kirinmaru.core.domain

import stream.reconfig.kirinmaru.core.NovelDetail

/**
 * Stub NovelDetail class for IPC
 */
data class CoreNovelDetail(
  override val origin: String,
  override val novelTitle: String,
  override val url: String,
  override val id: String? = null,
  override val tags: Set<String> = emptySet()
) : NovelDetail