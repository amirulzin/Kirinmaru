package stream.reconfig.kirinmaru.android

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Single

const val TEST_TAG = "TEST_TAG"

fun logd(string: String) {
  Log.d(TEST_TAG, string)
}

fun <T> Flowable<T>.logOnNext(msg: (T) -> Any? = { "" }): Flowable<T> = doOnNext { logd("OnNext: ${msg.invoke(it)}") }

fun <T> Single<T>.logOnSuccess(msg: (T) -> Any? = { "" }): Single<T> = doOnSuccess { logd("OnSuccess: ${msg.invoke(it)}") }