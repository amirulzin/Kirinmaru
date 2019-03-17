package commons.android.core.fragment

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.View
import commons.android.DrawerFragmentRecyclerBindingAlias
import commons.android.ProjectConstants
import commons.android.arch.offline.refresh.RemoteRefreshable

/**
 * Extensible list fragment containing a RecyclerView with a Toolbar enclosed by an [AppBarLayout]
 */
abstract class DrawerRecyclerFragment : DrawerFragment<DrawerFragmentRecyclerBindingAlias>(), RemoteRefreshable {
  override val layoutId = ProjectConstants.LYT_FRAGMENT_RECYCLER_VIEW

  override fun getToolbar() = binding.toolbar

  @CallSuper
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.refreshLayout.setColorSchemeColors(
      ContextCompat.getColor(context!!, ProjectConstants.CLR_ACCENT),
      ContextCompat.getColor(context!!, ProjectConstants.CLR_PRIMARY)
    )
  }

  override fun showRemoteIndicator(isShown: Boolean) {
    binding.remoteLoading.visibility = if (isShown) View.VISIBLE else View.GONE
  }
}
