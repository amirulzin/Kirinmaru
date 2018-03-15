package stream.reconfig.kirinmaru.android.ui.library

import stream.reconfig.kirinmaru.android.ui.domain.AbsChapterId
import stream.reconfig.kirinmaru.core.NovelDetail

data class LibraryItem(val novel: Novel, val latest: Chapter?, val currentRead: Chapter?) {

  data class Chapter(override val url: String) : AbsChapterId()

  data class Novel(
      override val origin: String,
      override val url: String,
      override val novelTitle: String,
      override val id: String?,
      override val tags: Set<String>
  ) : NovelDetail
}
