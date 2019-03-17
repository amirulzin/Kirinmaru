package stream.reconfig.kirinmaru.android.ui.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuItem
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.parcel.toParcel
import stream.reconfig.kirinmaru.android.ui.chapters.ChaptersFragment
import stream.reconfig.kirinmaru.android.ui.library.LibraryFragment
import stream.reconfig.kirinmaru.android.ui.novels.NovelsFragment
import stream.reconfig.kirinmaru.android.ui.reader.ReaderFragment
import stream.reconfig.kirinmaru.android.ui.reader.ReaderParcel
import stream.reconfig.kirinmaru.android.ui.settings.SettingsFragment
import stream.reconfig.kirinmaru.core.ChapterId
import stream.reconfig.kirinmaru.core.NovelDetail

/**
 * Idempotent FragmentNavigator to handle lateral and vertical app navigation.
 *
 * No references were held in here except plain constants.
 */
object FragmentNavigator {

  private const val container = R.id.drawerContentFrame

  @JvmStatic
  fun toChapters(activity: FragmentActivity, novel: NovelDetail) {
    val parcel = novel.toParcel()
    navigate("chapters", activity, true, ChaptersFragment.createArguments(parcel)) {
      ChaptersFragment.newInstance(parcel)
    }
  }

  @JvmStatic
  fun toReader(activity: FragmentActivity, novel: NovelDetail, chapter: ChapterId) {
    val parcel = ReaderParcel(novel.toParcel(), chapter.toParcel())
    navigate("reader", activity, true, ReaderFragment.createArguments(parcel)) {
      ReaderFragment.newInstance(parcel)
    }
  }

  @JvmStatic
  fun sideNavigate(activity: FragmentActivity, item: MenuItem, menu: Menu, firstOrigin: String): Boolean {

    if (item.isChecked) return true

    val tag = item.title.toString()
    val fm = activity.supportFragmentManager
    val frag = fm.findFragmentByTag(tag)

    val outFrag = frag ?: when (item.itemId) {
      R.id.navLibrary -> LibraryFragment.newInstance()
      R.id.navCatalogues -> NovelsFragment.newInstance(firstOrigin)
      R.id.navSettings -> SettingsFragment.newInstance()
      else -> return true
    }

    fm.beginTransaction()
      .replace(container, outFrag, tag)
      .commit()
    return true
  }

  @JvmStatic
  private inline fun navigate(
    tag: String,
    activity: FragmentActivity,
    toBackStack: Boolean,
    updateBundle: Bundle,
    crossinline creator: () -> Fragment
  ) {
    val fm = activity.supportFragmentManager
    val frag = fm.findFragmentByTag(tag)?.apply { arguments = updateBundle } ?: creator()
    fm.beginTransaction()
      .replace(container, frag, tag)
      .apply { if (toBackStack) addToBackStack(tag) }
      .commit()
  }
}