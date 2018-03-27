package stream.reconfig.kirinmaru.android.ui.common.refresh

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import stream.reconfig.kirinmaru.android.util.offline.ResourceState
import stream.reconfig.kirinmaru.android.util.offline.ResourceType
import stream.reconfig.kirinmaru.android.util.offline.State.*

/**
 * Handles ResourceState updates to relevant views.
 *
 * This also assume that all LOADING state is also executing a remote call.
 */
object ResourceStateHandler {

  @JvmStatic
  fun handleStateUpdates(
      coordinatorLayout: CoordinatorLayout,
      refreshLayout: SwipeRefreshLayout,
      resourceState: ResourceState,
      remoteRefreshable: RemoteRefreshable
  ) {
    with(refreshLayout) {
      when (resourceState.state) {
        COMPLETE -> isRefreshing = false
        LOADING -> isRefreshing = true
        ERROR -> {
          isRefreshing = false
          Snackbar.make(coordinatorLayout, resourceState.message, Snackbar.LENGTH_SHORT).show()
        }
      }
    }
    with(resourceState) {
      when {
        state == LOADING -> remoteRefreshable.showRemoteIndicator(true)
        state != LOADING && type == ResourceType.REMOTE -> remoteRefreshable.showRemoteIndicator(false)
      }
    }
  }
}