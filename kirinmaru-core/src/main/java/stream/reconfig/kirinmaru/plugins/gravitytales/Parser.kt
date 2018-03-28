package stream.reconfig.kirinmaru.plugins.gravitytales

import okhttp3.HttpUrl
import stream.reconfig.kirinmaru.core.LinkTransformer
import stream.reconfig.kirinmaru.core.NovelDetail
import stream.reconfig.kirinmaru.core.domain.CoreChapterDetail
import stream.reconfig.kirinmaru.core.domain.CoreNovelDetail
import stream.reconfig.kirinmaru.core.parser.AbsChapterDetailParser
import stream.reconfig.kirinmaru.core.parser.AbsIndexParser

internal object GravityTalesIndexParser : AbsIndexParser(
    selector = ".multi-column-dropdown a[href*=/novel/]",
    transformer = { CoreNovelDetail(GRAVITYTALES_ORIGIN, it.text(), it.attr("href")) }
)

internal class GravityTalesChapterDetailParser(novelDetail: NovelDetail) : AbsChapterDetailParser(
    rawText = "#chapterContent",
    nextUrl = ".chapter-navigation a:contains(next chapter)",
    prevUrl = ".chapter-navigation a:contains(previous chapter)",
    clean = {
      val nextUrl = chapterSlug(it.nextUrl)?.isValidSlug(novelDetail.url)
      val prevUrl = chapterSlug(it.previousUrl)?.isValidSlug(novelDetail.url)
      CoreChapterDetail(it.rawText, nextUrl, prevUrl)
    }
) {
  companion object {
    @JvmStatic
    private fun chapterSlug(url: String?) = url?.splitToSequence("/")?.filter { it.isNotBlank() }?.last()

    @JvmStatic
    private fun String?.isValidSlug(novelUrl: String): String? {
      //returns null if it equals novel main url or slug
      return if (this?.equals(novelUrl, true) == true) null else this
    }
  }
}

internal object GravityTalesLinkTransformer : LinkTransformer {
  override val baseUrl = HttpUrl.parse(GRAVITYTALES_HOME)!!
}


