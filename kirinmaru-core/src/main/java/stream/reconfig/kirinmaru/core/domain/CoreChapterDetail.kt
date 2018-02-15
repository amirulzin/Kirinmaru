package stream.reconfig.kirinmaru.core.domain

import stream.reconfig.kirinmaru.core.ChapterDetail

/**
 * Stub  CoreChapterDetail class for IPC
 */
data class CoreChapterDetail(
    override val rawText: String?,
    override val nextUrl: String?,
    override val previousUrl: String?
) : ChapterDetail