package stream.reconfig.kirinmaru.core.taxonomy;

/**
 * This will attempt to deduce next chapter by the given taxonomic number
 */

public class PredictiveTaxonomy {

  private final String absoluteUrl;
  private final String taxonomicNumber;
  private final IndexedTaxon taxon;
  private final int chapterIndex;
  private final int bookIndex;
  private final int bookDigitEnd;
  private final int sectionIndex;
  private final int sectionDigitEnd;
  private final int chapterDigitEnd;

  private boolean chapterAttempted = false, chapterBookAttempted = false, chapterBookDualAttempted = false;
  private boolean sectionAttempted = false, sectionChapterAttempted = false, sectionChapterBookAttempted = false, sectionChapterBookDualAttempted = false;

  private int chapterNumber = -1;
  private int sectionNumber = -1;
  private int bookNumber = -1;

  public PredictiveTaxonomy(String absoluteUrl) {
    this.absoluteUrl = absoluteUrl;
    this.taxonomicNumber = Taxonomy.createTaxonomicNumber(absoluteUrl);

    taxon = Taxonomy.createTaxon(absoluteUrl);
    sectionIndex = taxon.getSectionIndex();
    sectionDigitEnd = getDigitEnd(sectionIndex);
    chapterIndex = taxon.getChapterIndex();
    bookIndex = taxon.getBookIndex();
    bookDigitEnd = getDigitEnd(bookIndex);
    chapterDigitEnd = getDigitEnd(chapterIndex);
  }

  private int getDigitEnd(int indexBegin) {
    int end = indexBegin;
    do {
      end++;
      if (end >= absoluteUrl.length()) return end;
    } while (Character.isDigit(absoluteUrl.charAt(end)));
    return end;
  }

  //  @Nullable
  public <T> T predictNext(CompatFunction<String, T> attemptNextUrlFunction) {
    T t = null;
    String url;
    do {
      url = predict();
      if (url != null && !url.isEmpty()) {
        try {
          t = attemptNextUrlFunction.map(url);
          if (t instanceof Boolean) {
            if (Boolean.TRUE.equals(t)) break;
          } else if (t != null) break;
        } catch (Exception ignored) {

        }
      }
    } while (url != null && !url.isEmpty());
    return t;
  }

  //  @Nullable
  private String predict() { //ranges from 1 to 7 attempts per url

    if (taxon.isSectionSet) { //section
      if (!sectionAttempted) {
        sectionAttempted = true;
        String section = Taxonomy.View.getSection(taxonomicNumber);
        if (!section.isEmpty()) {
          sectionNumber = Integer.parseInt(section);
          return createIncrementedString(sectionIndex, sectionDigitEnd, sectionNumber + 1); //s++
        }
      } else if (taxon.isChapterSet) { //section - chapter
        if (!sectionChapterAttempted) {
          sectionChapterAttempted = true;
          String chapter = Taxonomy.View.getChapter(taxonomicNumber);
          if (!chapter.isEmpty()) {
            chapterNumber = Integer.parseInt(chapter);
            String sectionReset = createIncrementedString(sectionIndex, getDigitEnd(sectionIndex), 1);
            return createIncrementedString(sectionReset, chapterIndex, getDigitEnd(chapterIndex), chapterNumber + 1); //c++ s 1
          }
        } else if (taxon.isBookSet) { //section - chapter - book
          if (!sectionChapterBookAttempted) {
            sectionChapterBookAttempted = true;
            String book = Taxonomy.View.getBook(taxonomicNumber);
            if (!book.isEmpty()) {
              bookNumber = Integer.parseInt(book);
              String sectionReset = createIncrementedString(sectionIndex, getDigitEnd(sectionIndex), 1);
              String chapterReset = createIncrementedString(sectionReset, chapterIndex, getDigitEnd(chapterIndex), 1);
              return createIncrementedString(chapterReset, bookIndex, getDigitEnd(bookIndex), bookNumber + 1); //b++ c 1 s 1
            }
          } else if (!sectionChapterBookDualAttempted) {
            sectionChapterBookDualAttempted = true;
            String sectionReset = createIncrementedString(sectionIndex, getDigitEnd(sectionIndex), 1);
            String chapterIncremented = createIncrementedString(sectionReset, chapterIndex, getDigitEnd(chapterIndex), chapterNumber + 1);
            return createIncrementedString(chapterIncremented, bookIndex, getDigitEnd(bookIndex), bookNumber + 1); //b++ c++ s 1
          }
        }
      }
    } else {
      if (taxon.isChapterSet) { //chapter
        if (!chapterAttempted) {
          chapterAttempted = true;
          String chapter = Taxonomy.View.getChapter(taxonomicNumber);
          if (!chapter.isEmpty()) {
            chapterNumber = Integer.parseInt(chapter);
            return createIncrementedString(chapterIndex, chapterDigitEnd, chapterNumber + 1); //c++
          }
        } else if (taxon.isBookSet) { //chapter - book
          if (!chapterBookAttempted) {
            chapterBookAttempted = true;
            String book = Taxonomy.View.getBook(taxonomicNumber);
            if (!book.isEmpty()) {
              bookNumber = Integer.parseInt(book);
              String chapterReset = createIncrementedString(chapterIndex, chapterDigitEnd, 1);
              return createIncrementedString(chapterReset, bookIndex, bookDigitEnd, bookNumber + 1); //b++ c1
            }
          } else if (!chapterBookDualAttempted) {
            chapterBookDualAttempted = true;
            String chapterIncremented = createIncrementedString(chapterIndex, chapterDigitEnd, chapterNumber + 1);
            return createIncrementedString(chapterIncremented, bookIndex, bookDigitEnd, bookNumber + 1); //b++ c++
          }
        }
      }
    }
    return null;
  }


  //  @NonNull
  private String createIncrementedString(int startIndex, int endIndex, int targetNumber) {
    return createIncrementedString(absoluteUrl, startIndex, endIndex, targetNumber);
  }

  //  @NonNull
  private String createIncrementedString(String targetString, int startIndex, int endIndex, int targetNumber) {
    return new StringBuilder(targetString).replace(startIndex, endIndex, String.valueOf(targetNumber))
      .toString();
  }

}
