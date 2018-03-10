package stream.reconfig.kirinmaru.android.ui.taxonomy;

import java.util.Locale;

/**
 *
 */
@SuppressWarnings("WeakerAccess")
public class TaxonomyUtil {

  private static final int INDEX_NOT_FOUND = -1;

  /**
   * This method is taken from Apache Commons StringUtils stripStart method
   * <p>
   * <a href="http://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html">Source</a>
   *
   * @param str        Original string
   * @param stripChars Chars to be removed
   * @return String with given chars removed from beginning
   */
  public static String stripStart(final String str, final String stripChars) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return str;
    }
    int start = 0;
    if (stripChars == null) {
      while (start != strLen && Character.isWhitespace(str.charAt(start))) {
        start++;
      }
    } else if (stripChars.isEmpty()) {
      return str;
    } else {
      while (start != strLen && stripChars.indexOf(str.charAt(start)) != INDEX_NOT_FOUND) {
        start++;
      }
    }
    return str.substring(start);
  }

  public static String replaceFromLast(String inputString, String targetMold) {

    char[] inputChars = inputString.toCharArray();
    char[] segmentTarget = targetMold.toCharArray(); //remember, this is new primitive
    if (inputChars.length > segmentTarget.length - 1)
      throw new IllegalStateException("Larger input for target: " + new String(segmentTarget) + " and input:" + inputString);
    for (int i = inputString.length() - 1, t = segmentTarget.length - 1; i >= 0; i--, t--) {
      segmentTarget[t] = inputChars[i];
    }
    return new String(segmentTarget);
  }

  public static String formatUrl(String url) {
    String urlTrim = url.trim().toLowerCase(Locale.US);
    if (urlTrim.charAt(url.length() - 1) != '/') {
      return urlTrim.concat("/");
    }
    return urlTrim;
  }

  public static String getLastSegment(String path) {
    int lastSlash = path.lastIndexOf("/");
    if (lastSlash == path.length() - 1) {
      return path.substring(path.substring(0, lastSlash).lastIndexOf("/"), path.length());
    } else {
      return path.substring(lastSlash, path.length());
    }
  }
}
