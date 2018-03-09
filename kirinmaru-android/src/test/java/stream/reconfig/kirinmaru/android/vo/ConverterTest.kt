package stream.reconfig.kirinmaru.android.vo

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test for string set TypeConverter (Room)
 */
class ConverterTest {
  @Test
  fun intoString() {
    val x = Novel.Converter().fromSet(setOf("a", "b", "c"))
    assertTrue(x == "~%a~%b~%c")
  }

  @Test
  fun intoSet() {
    val x = Novel.Converter().toSet("~%a~%b~%c")
    val arr = arrayOf("a", "b", "c")
    x.forEachIndexed { i, s -> assertTrue(arr[i] == s) }
  }
}