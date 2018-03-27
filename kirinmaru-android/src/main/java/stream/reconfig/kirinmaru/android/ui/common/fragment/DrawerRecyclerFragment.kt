package stream.reconfig.kirinmaru.android.ui.common.fragment

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.View
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.FragmentRecyclerBinding
import stream.reconfig.kirinmaru.android.ui.common.refresh.RemoteRefreshable

/**
 * Extensible list fragment containing a RecyclerView with a Toolbar enclosed by an [AppBarLayout]
 */
abstract class DrawerRecyclerFragment : DrawerFragment<FragmentRecyclerBinding>(), RemoteRefreshable {
  override val layoutId = R.layout.fragment_recycler

  override fun getToolbar() = binding.toolbar

  @CallSuper
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.refreshLayout.setColorSchemeColors(
        ContextCompat.getColor(context!!, R.color.colorAccent),
        ContextCompat.getColor(context!!, R.color.colorPrimary)
    )
  }

  override fun showRemoteIndicator(isShown: Boolean) {
    binding.remoteLoading.visibility = if (isShown) View.VISIBLE else View.GONE
  }
}
