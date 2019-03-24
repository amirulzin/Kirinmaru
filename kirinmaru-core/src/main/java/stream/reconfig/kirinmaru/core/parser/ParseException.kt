package stream.reconfig.kirinmaru.core.parser

import java.io.IOException

data class ParseException(
  override val message: String,
  val origin: String = ""
) : IOException("$origin: $message")