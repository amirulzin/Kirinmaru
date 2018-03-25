package stream.reconfig.kirinmaru.android.ui.reader

import android.app.FragmentManager

import android.support.annotation.ColorInt
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.ViewReaderSettingBinding
import stream.reconfig.kirinmaru.android.di.scopes.ActivityScope
import stream.reconfig.kirinmaru.android.ui.numberpicker.NumberPicker

@ActivityScope
data class ReaderSettingHelper(
    private val fragmentManager: FragmentManager,
    private val binding: ViewReaderSettingBinding,
    private val readerSetting: ReaderSetting,
    private val fonts: List<String>,
    private val listener: Listener
) {

  init {
    with(binding) {
      setting = readerSetting

      fontSpinner.apply {
        adapter = ArrayAdapter<String>(binding.root.context, R.layout.item_spinner_fonts, R.id.textView, fonts)
        setSelection(fonts.indexOf(readerSetting.fontName))
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
          override fun onNothingSelected(parent: AdapterView<*>?) {}

          override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            result = result.copy(fontName = fonts[position])
          }
        }
      }

      pickerFontSize!!.picker = NumberPicker(readerSetting.fontSizeSp)
      pickerLetterSpacing!!.picker = NumberPicker(readerSetting.letterSpacingSp)
      pickerLineSpacing!!.picker = NumberPicker(readerSetting.lineSpacingExtra)

      backgroundColorButton.setOnClickListener { launchColorPicker(backgroundColorId()) }

      textColorButton.setOnClickListener { launchColorPicker(textColorId()) }

      isGlobal.setOnCheckedChangeListener { _, isChecked -> result = readerSetting.copy(isGlobal = !isChecked) }

      settingsApply.setOnClickListener { listener.onComplete(retrieveResult()) }
      settingsCancel.setOnClickListener { listener.onCancel() }
    }
  }

  private var result: ReaderSetting = readerSetting.copy()

  private val colorPickerListener = object : ColorPickerDialogListener {
    override fun onDialogDismissed(dialogId: Int) {

    }

    override fun onColorSelected(dialogId: Int, color: Int) {
      when (dialogId) {
        backgroundColorId() -> setBackgroundColor(color)
        textColorId() -> setTextColor(color)
      }
    }
  }

  private fun launchColorPicker(int: Int) {
    ColorPickerDialog.newBuilder()
        .setShowAlphaSlider(false)
        .setDialogId(int)
        .create()
        .apply { setColorPickerDialogListener(colorPickerListener) }
        .show(fragmentManager, "color-picker-dialog")
  }

  private fun retrieveResult() = result.copy(
      fontSizeSp = binding.pickerFontSize!!.picker!!.number,
      letterSpacingSp = binding.pickerLetterSpacing!!.picker!!.number,
      lineSpacingExtra = binding.pickerLineSpacing!!.picker!!.number
  )

  private fun setBackgroundColor(@ColorInt color: Int) {
    binding.backgroundColorButton.setBackgroundColor(color)
    result = result.copy(backgroundColor = color)
  }

  private fun setTextColor(@ColorInt color: Int) {
    binding.textColorButton.setBackgroundColor(color)
    result = result.copy(fontColor = color)
  }

  fun backgroundColorId() = 1

  fun textColorId() = 2

  interface Listener {
    fun onComplete(readerSetting: ReaderSetting)
    fun onCancel()
  }
}