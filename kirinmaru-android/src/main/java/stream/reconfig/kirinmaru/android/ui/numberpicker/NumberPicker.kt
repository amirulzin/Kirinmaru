package stream.reconfig.kirinmaru.android.ui.numberpicker

data class NumberPicker(var number: Int) {
  fun text() = number.toString()
  fun increment() = number++
  fun decrement() = number--
}