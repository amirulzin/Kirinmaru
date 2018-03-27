package stream.reconfig.kirinmaru.android.ui.favorites

import stream.reconfig.kirinmaru.core.NovelDetail

data class FavoriteNovel(
    override val origin: String,
    override val url: String,
    override val novelTitle: String,
    override val id: String?,
    override val tags: Set<String>
) : NovelDetail