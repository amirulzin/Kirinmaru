package stream.reconfig.kirinmaru.android.ui.reader

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.assets.Fonts

data class ReaderSetting(
    val settingKey: String,
    val fontSizeSp: Int,
    val letterSpacingSp: Int,
    val lineSpacingExtra: Int,
    val fontName: String,
    @ColorInt val fontColor: Int,
    @ColorInt val backgroundColor: Int,
    val isGlobal: Boolean
) {
  companion object {
    const val KEY_GLOBAL = "global"

    @JvmStatic
    fun default(context: Context) = ReaderSetting(
        settingKey = KEY_GLOBAL,
        fontSizeSp = 16,
        letterSpacingSp = 0,
        lineSpacingExtra = 0,
        fontName = Fonts.DEFAULT_TYPEFACE_NAME,
        fontColor = ContextCompat.getColor(context, R.color.colorTextPrimary),
        backgroundColor = ContextCompat.getColor(context, R.color.colorWindowBackground),
        isGlobal = true
    )
  }
}

