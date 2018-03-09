package stream.reconfig.kirinmaru.android.ui.navigation

import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuItem
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.ui.chapters.ChaptersFragment
import stream.reconfig.kirinmaru.android.ui.novels.NovelItem
import stream.reconfig.kirinmaru.android.ui.novels.NovelsFragment

/**
 * Idempotent FragmentNavigator to handle lateral and vertical app navigation.
 *
 * No references were held in here except plain constants.
 */
object FragmentNavigator {
  private const val container = R.id.drawerContentFrame

  @JvmStatic
  fun toChapters(activity: FragmentActivity, novel: NovelItem) {
    val tag = "chapters"
    val fm = activity.supportFragmentManager
    val frag = fm.findFragmentByTag(tag) ?: ChaptersFragment.newInstance(novel)

    activity.supportFragmentManager
        .beginTransaction()
        .replace(container, frag, tag)
        .addToBackStack(tag)
        .commit()
  }

  @JvmStatic
  fun sideNavigate(activity: FragmentActivity, item: MenuItem, menu: Menu, firstOrigin: String): Boolean {

    if (item.isChecked) return true

    val tag = item.title.toString()
    val fm = activity.supportFragmentManager
    val frag = fm.findFragmentByTag(tag)

    val outFrag = frag ?: when (item.itemId) {
      R.id.navCatalogues -> NovelsFragment.newInstance(firstOrigin)
      R.id.navSettings -> return true
      else -> throw NotImplementedError("Menu id case not implemented")
    }

    fm.beginTransaction()
        .replace(container, outFrag, tag)
        .commit()
    return true
  }
}