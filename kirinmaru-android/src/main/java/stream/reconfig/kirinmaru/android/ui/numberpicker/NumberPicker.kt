package stream.reconfig.kirinmaru.android.ui.numberpicker

import android.databinding.BaseObservable
import android.databinding.Bindable

data class NumberPicker(@Bindable var number: Int) : BaseObservable() {
  fun text() = number.toString()
  fun increment() {
    number++
    notifyChange()
  }

  fun decrement() {
    number--
    notifyChange()
  }
}