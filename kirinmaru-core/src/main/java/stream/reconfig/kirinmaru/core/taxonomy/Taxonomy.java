package stream.reconfig.kirinmaru.core.taxonomy;

import java.util.Locale;

/**
 * Taxonomy class where it will spew out numbers in form of book, chapter, part, section
 * from a given url. See {@link #createTaxonomicNumber}
 * <p>
 * The final format is handled in {@link Taxon#toString()}.
 * <p>
 * Note: Code in this class is fairly optimized since it may run on UI thread
 */
@SuppressWarnings("UnnecessaryContinue")
public class Taxonomy {

  //Currently index of the identifier (b, c ,s, p) is hard coded in Taxonomy.View
  static final String FILL_CHAR = "0";
  static final String BOOK_MOLD = "b" + fill(3);
  static final String CHAPTER_MOLD = "c" + fill(5); //enough for 99999 chapters
  static final String SECTION_MOLD = "s" + fill(3);
  static final String PART_MOLD = "p" + fill(3);
  private static final String[] BOOKS_ID = {"book", "vol", "volume"};
  private static final String[] CHAPTERS_ID = {"chapter", "ch"};

  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz"; //currently used in situational post process

  private Taxonomy() {}

  public static String createTaxonomicNumber(String url) {
    return createTaxon(url).toString();
  }

  public static String createTaxonomicDisplay(String taxonomicNumber) {
    return Taxonomy.View.getDisplayable(taxonomicNumber);
  }

  static IndexedTaxon createTaxon(String url) {
    String path = url;
    if (path.contains("prologue")) {
      path = path.substring(0, path.length())
        .concat("-chapter-0"); //length - 1 to exclude the last '/' returned from formatted url
    }

    String lastSegment = TaxonomyUtil.getLastSegment(path);
    //

    try {
      return parseSegment(lastSegment + "/"); //TODO FIX THIS. Currently iteration need a dummy last char so substring can work even on single character
    } catch (IllegalStateException | IndexOutOfBoundsException e) {
      throw new IllegalStateException(String.format(Locale.US, "%10s %s\nError: [%s]", "Problem", url, e.getMessage()), e);
    }
    //return new IndexedTaxon(); //return default 0 on failure
  }

  /**
   * Essentially it will parse each segment of grouped digits
   * and set each of those to an IndexedTaxon
   */
  private static IndexedTaxon parseSegment(String lastSegment) { //formatted: lowercase, trimmed, & ending with '/'
    IndexedTaxon result = new IndexedTaxon();

    char[] chars = lastSegment.toCharArray();
    boolean found = false;

    //Declare things that we might need for post process. These vars start with `post` for visibility
    int postChapterDigitIndex = -1;

    //Finally begin
    int textBegin = 0;
    int digitBegin = -2; // -1 to reset. -2 if digit was ever found
    for (int i = 0; i < chars.length; i++) {

      char c = chars[i];

      //If not found (when previous char is not a digit)
      if (!found) {
        if (isDigit(c)) {
          found = true;
          digitBegin = i;
          continue;
        }
      } else {
        //If previously a digit
        if (isDigit(c)) {
          continue;
        } else {

          //Handle found section. Currently i = non digit character, just right after a found digit

          String searchSegment = lastSegment.substring(textBegin, digitBegin);
          String targetSegment = lastSegment.substring(digitBegin, i);
          int absoluteDigitIndex = digitBegin;

          if (!result.isChapterSet) { // we treat anything after chapters as parts and section

            if (isBookSegment(searchSegment)) {
              result.setBook(absoluteDigitIndex, TaxonomyUtil.replaceFromLast(targetSegment, BOOK_MOLD));
            } else if (isChapterSegment(searchSegment)) {
              result.setChapter(absoluteDigitIndex, TaxonomyUtil.replaceFromLast(targetSegment, CHAPTER_MOLD));
              postChapterDigitIndex = i;
            }

          } else {
            //Handle digits found if chapter is already set
            if (!result.isSectionSet) {
              result.setSection(absoluteDigitIndex, TaxonomyUtil.replaceFromLast(targetSegment, SECTION_MOLD));
            } else {
              result.setPart(absoluteDigitIndex, TaxonomyUtil.replaceFromLast(targetSegment, PART_MOLD));
              break; //Stop if part is found
            }
          }

          //Prepare flags for next section
          found = false;
          digitBegin = -1;
          textBegin = i;
        }
      }
    }

    if (postChapterDigitIndex != -1) {
      result = processEndingLettersPostChapterDigit(postChapterDigitIndex, lastSegment, result);
    }

    return result;
  }

  private static String fill(int length) {
    String str = "";
    for (int i = 0; i < length; i++) {
      str = str.concat(FILL_CHAR);
    }
    return str;
  }

  private static boolean isDigit(char c) {
    return Character.isDigit(c);
  }

  /**
   * Because I love WDQK and pretty much its the initial reason why this method is implemented.
   * <p>
   * This execute only when there's a letter right after the chapter number. e.g.
   * <blockquote>http://www.wuxiaworld.com/wdqk-index/wdqk-chapter-577b/</blockquote>
   * <p>
   * Without it, all works fine. This might handle future novels that might use WDQK style of denoting parts/section.
   * <p>
   * Note: Only applies if chapter is already set and parts isn't set. Section is too useful, so this won't handle that.
   */
  private static <T extends Taxon> T processEndingLettersPostChapterDigit(int nonDigitPostChapterDigit, String lastSegment, T result) {

    if (result.isChapterSet && !result.isPartSet) {
      char c = lastSegment.charAt(nonDigitPostChapterDigit);
      if (isLetter(c)) {
        int part = ALPHABET.indexOf(c);
        result.setPart(TaxonomyUtil.replaceFromLast(String.valueOf(part < 0 ? 0 : part), PART_MOLD));
      }
    }
    return result;
  }

  private static boolean isLetter(char c) {
    return Character.isLetter(c);
  }

  private static boolean isBookSegment(String input) {
    for (int i = 0; i < BOOKS_ID.length; i++) {
      if (input.contains(BOOKS_ID[i])) return true;
    }
    return false;
  }

  private static boolean isChapterSegment(String input) {
    for (int i = 0; i < CHAPTERS_ID.length; i++) {
      if (input.contains(CHAPTERS_ID[i])) return true;
    }
    return false;
  }

  /**
   * Allows taxonomic number to be human readable.
   * Currently this has lots of hardcoded stuff.
   */
  @SuppressWarnings("WeakerAccess")
  public static class View {

    //Sample taxonomicNumber b0000c00577s0000p0001
    private static final String STUB = new Taxon().toString();
    private static final int BOOK_BEGIN = STUB.indexOf(BOOK_MOLD.charAt(0));
    private static final int CHAPTER_BEGIN = STUB.indexOf(CHAPTER_MOLD.charAt(0));
    private static final int SECTION_BEGIN = STUB.indexOf(SECTION_MOLD.charAt(0));
    private static final int PART_BEGIN = STUB.indexOf(PART_MOLD.charAt(0));

    private View() {}

    public static String getBook(String taxonomicNumber) {
      return TaxonomyUtil.stripStart(taxonomicNumber.substring(BOOK_BEGIN + 1, CHAPTER_BEGIN), FILL_CHAR);
    }

    public static String getChapter(String taxonomicNumber) {
      return TaxonomyUtil.stripStart(taxonomicNumber.substring(CHAPTER_BEGIN + 1, SECTION_BEGIN), FILL_CHAR);
    }

    public static String getSection(String taxonomicNumber) {
      return TaxonomyUtil.stripStart(taxonomicNumber.substring(SECTION_BEGIN + 1, PART_BEGIN), FILL_CHAR);
    }

    public static String getPart(String taxonomicNumber) {
      return TaxonomyUtil.stripStart(taxonomicNumber.substring(PART_BEGIN + 1), FILL_CHAR);
    }

    public static String getDisplayable(String taxonomicNumber) {
      String result = "";
      String b = View.getBook(taxonomicNumber);
      String c = View.getChapter(taxonomicNumber);
      String s = View.getSection(taxonomicNumber);
      String p = View.getPart(taxonomicNumber);
      if (!b.isEmpty()) result = result.concat("Book " + b);
      if (!c.isEmpty()) result = result.concat(" Chapter " + c);
      else result = result.concat("Chapter 0"); //happens when c00000, and 0 is stripped
      if (!s.isEmpty()) result = result.concat(" - " + s);
      if (!p.isEmpty()) result = result.concat(" - " + p);
      return result.trim();
    }
  }
}
