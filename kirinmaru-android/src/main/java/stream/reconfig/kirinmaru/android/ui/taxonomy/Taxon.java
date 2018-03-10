package stream.reconfig.kirinmaru.android.ui.taxonomy;

/**
 * Helper class to check changed properties and hold the final result pre-taxonomized.
 * <p>
 * The taxonomic number can be obtained from toString().
 */
@SuppressWarnings("WeakerAccess")
class Taxon {

  boolean isBookSet = false;
  boolean isChapterSet = false;
  boolean isPartSet = false;
  boolean isSectionSet = false;
  private String book;
  private String chapter;
  private String section;
  private String part;

  /**
   * Returns a full formatted string. Nulls default to 0 (the molds handle filling that).
   * Sample: /ti-vol-13-chapter-10-1/
   * Becomes: b0013c00010s0001p0000
   */
  @Override
  public String toString() {
    // Android Studio hinted for this optimization. And I ate the bait.
    return (book != null ? book : Taxonomy.BOOK_MOLD)
        + (chapter != null ? chapter : Taxonomy.CHAPTER_MOLD)
        + (section != null ? section : Taxonomy.SECTION_MOLD)
        + (part != null ? part : Taxonomy.PART_MOLD);
  }

  Taxon setBook(String book) {
    isBookSet = true;
    this.book = book;
    return this;
  }

  Taxon setChapter(String chapter) {
    isChapterSet = true;
    this.chapter = chapter;
    return this;
  }

  Taxon setSection(String section) {
    isSectionSet = true;
    this.section = section;
    return this;
  }

  Taxon setPart(String part) {
    isPartSet = true;
    this.part = part;
    return this;
  }
}
