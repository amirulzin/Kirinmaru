package stream.reconfig.kirinmaru.android.util.fullscreen

import android.support.v4.app.FragmentActivity
import android.view.WindowManager

object FullScreenUtil {

  @JvmStatic
  fun exitFullScreen(activity: FragmentActivity?) {
    activity?.window?.apply {
      clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
  }

  @JvmStatic
  fun enterFullscreen(activity: FragmentActivity?) {
    activity?.window?.apply {
      setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
  }
}