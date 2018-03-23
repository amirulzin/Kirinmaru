package stream.reconfig.kirinmaru.android.ui.settings

import android.os.Bundle
import android.view.View
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.databinding.FragmentScrollableBinding
import stream.reconfig.kirinmaru.android.ui.common.fragment.DrawerFragment

class SettingsFragment : DrawerFragment<FragmentScrollableBinding>() {
  companion object {
    @JvmStatic
    fun newInstance() = SettingsFragment()
  }

  override fun getToolbar() = binding.toolbar

  override val layoutId = R.layout.fragment_scrollable

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    getToolbar().title = "Preference"

    if (activity?.isFinishing == false) {
      view.post {
        childFragmentManager.beginTransaction()
            .replace(binding.contentContainer.id, PrefChildFragment.newInstance())
            .commit()
      }
    }
  }
}


