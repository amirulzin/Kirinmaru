package stream.reconfig.kirinmaru.android.vo

import stream.reconfig.kirinmaru.core.ChapterId

data class DBChapterId(
  override val url: String,
  override val title: String?
) : ChapterId