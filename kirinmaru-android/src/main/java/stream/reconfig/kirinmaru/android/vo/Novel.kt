package stream.reconfig.kirinmaru.android.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import stream.reconfig.kirinmaru.core.NovelDetail

/**
 * Novel entity
 */
@Entity
@TypeConverters(Novel.Converter::class)
data class Novel(
    override val id: String?,
    override val novelTitle: String,
    @PrimaryKey override val url: String,
    override val tags: Set<String>,
    override val origin: String
) : NovelDetail {

  internal class Converter {

    private val delimiter = "~%"

    @TypeConverter
    fun fromSet(set: Set<String>): String {
      if (set.isEmpty()) return ""
      val sb = StringBuilder(set.size)
      for (s in set) {
        if (s.isNotBlank()) {
          sb.append(delimiter)
          sb.append(s)
        }
      }
      return sb.toString()
    }

    @TypeConverter
    fun toSet(string: String): Set<String> {
      val split = string.split(delimiter)
      if (split.isEmpty()) return emptySet()
      val result = LinkedHashSet<String>(split.size)
      split.filterTo(result) { it.isNotBlank() }
      return result
    }
  }
}
