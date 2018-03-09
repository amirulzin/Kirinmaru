package stream.reconfig.kirinmaru.android.util.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Add this disposable to the passed CompositeDisposable
 */
fun Disposable.addTo(c: CompositeDisposable) = c.add(this)