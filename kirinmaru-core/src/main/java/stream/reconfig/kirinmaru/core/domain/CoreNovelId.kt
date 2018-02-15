package stream.reconfig.kirinmaru.core.domain

import stream.reconfig.kirinmaru.core.NovelId

/**
 * Stub NovelId class for IPC
 */
data class CoreNovelId(
    override val novelTitle: String,
    override val url: String,
    override val id: String? = null,
    override val tags: Set<String> = emptySet()
) : NovelId