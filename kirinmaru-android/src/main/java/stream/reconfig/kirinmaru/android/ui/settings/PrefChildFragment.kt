package stream.reconfig.kirinmaru.android.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import commons.android.dagger.compat.DaggerPreferenceFragmentCompat
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.BuildConfig
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.db.Database
import stream.reconfig.kirinmaru.android.prefs.FirstNavPref
import stream.reconfig.kirinmaru.android.prefs.SHARED_PREF_NAME
import javax.inject.Inject

class PrefChildFragment : DaggerPreferenceFragmentCompat() {
  companion object {
    @JvmStatic
    fun newInstance() = PrefChildFragment()
  }

  @Inject
  lateinit var database: Database

  @Inject
  lateinit var firstNavPref: FirstNavPref

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    preferenceManager.apply {
      sharedPreferencesName = SHARED_PREF_NAME
      sharedPreferencesMode = Context.MODE_PRIVATE
    }
    addPreferencesFromResource(R.xml.preferences)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setup()
  }

  private fun setup() {

    findPreference(getString(R.string.first_nav_key)).apply {
      Single.fromCallable { firstNavPref.load() }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe { successId, _ ->
            setDefaultValue(successId == R.id.navLibrary)
            setOnPreferenceChangeListener { _, newValue ->
              if (newValue as Boolean) firstNavPref.persist(R.id.navLibrary)
              else firstNavPref.persist(R.id.navCatalogues)
              true
            }
          }
    }

    findPreference(getString(R.string.reset_database_key)).setOnPreferenceClickListener {
      Completable.fromCallable { database.clearAllTables() }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              { toast("Database cleared") },
              {
                toast(it.message
                    ?: "Clearing database error. Try clearing the app storage via Android Settings")
              }
          )
      true
    }

    findPreference(getString(R.string.version_key))
        .summary = BuildConfig.VERSION_NAME
  }

  private fun toast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
  }
}