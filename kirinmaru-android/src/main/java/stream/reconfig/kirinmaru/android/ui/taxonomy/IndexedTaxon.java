package stream.reconfig.kirinmaru.android.ui.taxonomy;

/**
 * IndexedTaxon allows String indexes to be set and retrieved.
 * <p>
 * These indexes are currently used as flags to speed check
 * if they are set or not. If they were set, the class allows
 * retrieval to which part of the original string was the index set.
 */
public class IndexedTaxon extends Taxon {
  private int bookIndex = -1;
  private int chapterIndex = -1;
  private int sectionIndex = -1;
  private int partIndex = -1;

  public int getBookIndex() {
    return bookIndex;
  }

  public int getChapterIndex() {
    return chapterIndex;
  }

  public int getSectionIndex() {
    return sectionIndex;
  }

  public int getPartIndex() {
    return partIndex;
  }

  Taxon setBook(int index, String book) {
    bookIndex = index;
    return super.setBook(book);
  }

  Taxon setChapter(int index, String chapter) {
    chapterIndex = index;
    return super.setChapter(chapter);
  }

  Taxon setSection(int index, String section) {
    sectionIndex = index;
    return super.setSection(section);
  }

  Taxon setPart(int index, String part) {
    partIndex = index;
    return super.setPart(part);
  }
}
