package stream.reconfig.kirinmaru.plugins.wuxiaworld

/**
 * Slugs for old novels that are not easily found via the API and
 * not properly grouped to a proper parent ID.
 *
 * This implementation may change in the future but it serve its purpose for now.
 */
object OldNovels {
  val map: Map<String, Set<String>> = mapOf(
      "7-killers" to setOf(
          "7-killers-chapter-8",
          "7-killers-chapter-7",
          "7-killers-chapter-6",
          "7-killers-chapter-5",
          "7-killers-chapter-4",
          "7-killers-chapter-3",
          "7-killers-chapter-2",
          "7-killers-chapter-1"
      ),
      "dkwss" to setOf(
          "dkwss-chapter-25",
          "dkwss-chapter-24",
          "dkwss-chapter-23",
          "dkwss-chapter-22",
          "dkwss-chapter-21-2",
          "dkwss-chapter-21",
          "dkwss-chapter-20",
          "dkwss-chapter-19",
          "dkwss-chapter-18",
          "dkwss-chapter-17",
          "dkwss-chapter-16",
          "dkwss-chapter-15",
          "dkwss-chapter-14",
          "dkwss-chapter-13",
          "dkwss-chapter-12",
          "dkwss-chapter-11",
          "dkwss-chapter-10",
          "dkwss-chapter-9",
          "dkwss-chapter-8",
          "dkwss-chapter-7",
          "dkwss-chapter-6",
          "dkwss-chapter-5",
          "dkwss-chapter-4",
          "dkwss-chapter-3",
          "dkwss-chapter-2",
          "dkwss-chapter-1"
      ),
      "hsnt-index" to setOf(
          "hsnt-chapter-18",
          "hsnt-chapter-17",
          "hsnt-chapter-16",
          "hsnt-chapter-15",
          "hsnt-chapter-14",
          "hsnt-chapter-13",
          "hsnt-chapter-12",
          "heroes-shed-no-tears-chapter-11-eighty-eight-slain",
          "heroes-shed-no-tears-chapter-10-the-second-month-is-too-early-for-spring-in-luoyang",
          "heroes-shed-no-tears-chapter-9-die-wu",
          "heroes-shed-no-tears-chapter-8-no-turning-back",
          "heroes-shed-no-tears-chapter-7-the-lion-clan-hall-of-copper-camel-alley",
          "heroes-shed-no-tears-chapter-6-seven-story-buddhist-pagoda",
          "chapter-5-extraordinary-meetings-extraordinary-adventures",
          "heroes-shed-no-tears-chapter-4-an-extraordinary-person-an-extraordinary-place-extraordinary-things",
          "heroes-shed-no-tears-chapter-3-surprise-attack",
          "chapter-2-an-important-head",
          "chapter-1-a-solitary-box",
          "prologue"
      )
  )
}