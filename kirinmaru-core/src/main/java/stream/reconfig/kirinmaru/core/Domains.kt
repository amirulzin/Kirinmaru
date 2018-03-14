package stream.reconfig.kirinmaru.core

/**
 * NovelDetail used for listing novels. Respect non-null values.
 * It's up to the plugin how it resolves the given properties
 * @property id         Id can be anything as long as it is unique.
 * @property url        Url can be slugs (recommended), or complete link
 * @property tags       Set of tags .e.g. Korean, Completed
 */
interface NovelDetail {
  val novelTitle: String
  val url: String
  val id: String?
  val tags: Set<String>
}

/**
 * ChapterId used for finding Chapters.
 * It's up to the plugin how it resolves the given properties
 * @property url        Url can be a complete link, slug, or relative.
 */
interface ChapterId {
  val url: String
}

/**
 * ChapterDetail used for getting the actual text body
 * It's up to the plugin how it resolves the given properties
 * @property rawText    Untouched raw HTML fragment. Include the markups.
 *                      Avoid getting too much unnecessary outer elements. Can return empty string.
 *                      Can optionally return null to signal an error to upstream (e.g. normally happen during parsing broken link).
 * @property nextUrl    Next chapter url, if available. Must follow the same rule/format as the plugin definition of ChapterId 'url'
 * @property previousUrl Previous chapter url, if available. Must follow the same rule/format as the plugin definition of ChapterId 'url'
 */
interface ChapterDetail {
  val rawText: String?
  val nextUrl: String?
  val previousUrl: String?
}


