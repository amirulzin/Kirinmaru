package stream.reconfig.kirinmaru.android.ui.reader

import stream.reconfig.kirinmaru.android.R

data class ReaderSetting(
    val settingKey: String = "global", // or by novelUrl
    val fontSizeSp: Int = 14,
    val letterSpacingSp: Int = 0,
    val lineSpacingExtra: Int = 0,
    val fontName: String = "DEFAULT",
    val fontColor: Int = R.color.colorTextPrimary,
    val backgroundColor: Int = R.color.colorWindowBackground
)