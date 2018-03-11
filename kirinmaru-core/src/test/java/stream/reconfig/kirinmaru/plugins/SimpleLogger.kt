package stream.reconfig.kirinmaru.plugins

import java.lang.StringBuilder

class SimpleLogger(private val logging: Boolean) {
  private val sb = StringBuilder()
  fun log(str: Any?) {
    if (logging)
      synchronized(sb) {
        sb.appendln(str)
      }
  }

  fun printlog() {
    if (logging) println(sb.toString())
  }
}