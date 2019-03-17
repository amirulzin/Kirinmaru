package commons.android.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.UiThread
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Auto sampling TextWatcher. Attach in onResume and detach in onPause
 * */
@UiThread
abstract class RxTextWatcher : TextWatcher, LifecycleObserver {
  private val timeoutMs = 300L
  private var publisher: PublishSubject<String>? = null
  private val disposables = CompositeDisposable()

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun attach() {
    getTextView()?.let {
      it.addTextChangedListener(this)
      publisher = PublishSubject.create<String>().also { subject ->
        subject.debounce(timeoutMs, TimeUnit.MILLISECONDS)
          .distinctUntilChanged()
          .observeOn(AndroidSchedulers.mainThread())
          .map(::onPeriodicalChanged)
          .subscribeOn(Schedulers.computation())
          .subscribe()
          .addTo(disposables)
      }
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  fun detach() {
    getTextView()?.removeTextChangedListener(this)
    publisher = null
    disposables.dispose()
  }

  abstract fun getTextView(): TextView?

  abstract fun onPeriodicalChanged(input: String)

  override fun afterTextChanged(s: Editable?) {
    s?.let { publisher?.onNext(it.toString()) }
  }

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}