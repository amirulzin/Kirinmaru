package stream.reconfig.kirinmaru.android.util.validator

import android.os.Looper
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import java.io.IOException

/**
 * Simple thread validator for Android
 */
object ThreadValidator {

  @JvmStatic
  @WorkerThread
  fun validateWorkerThread() {
    if (Thread.currentThread() == Looper.getMainLooper().thread) throw IOException("Call invoked on Main Thread")
  }

  @JvmStatic
  @MainThread
  fun validateMainThread() {
    if (Thread.currentThread() != Looper.getMainLooper().thread) throw IOException("Call not invoked on main thread")
  }
}
