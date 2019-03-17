package commons.android.arch

import android.util.Log
import commons.android.BuildConfigAlias
import commons.android.ProjectConstants.DEBUG_TAG
import io.reactivex.functions.Consumer
import retrofit2.HttpException

class RetrofitRxErrorHandler : Consumer<Throwable> {

  lateinit var postMessage: (message: String) -> Unit

  override fun accept(error: Throwable?) {
    val msg: String = error?.let { err ->
      if (err is HttpException) err.response().errorBody()?.string() ?: "JSON error body is empty"
      else err.message ?: "Error message is empty"
    } ?: "Unknown error"
    if (BuildConfigAlias.DEBUG) {
      Log.e(DEBUG_TAG, msg)
      error?.printStackTrace()
    }
    postMessage(msg)
  }
}