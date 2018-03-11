package stream.reconfig.kirinmaru.plugins

import java.lang.StringBuilder

class SimpleLogger(private val logging: Boolean) {
  private val sb = StringBuilder()
  fun log(str: String) {
    if (logging)
      synchronized(sb) {
        sb.appendln(str)
      }
  }

  fun printlog() {
    println(sb.toString())
  }
}