package stream.reconfig.kirinmaru.android.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import stream.reconfig.kirinmaru.android.BuildConfig
import stream.reconfig.kirinmaru.android.R
import stream.reconfig.kirinmaru.android.db.Database
import stream.reconfig.kirinmaru.android.util.preference.DaggerPreferenceFragmentCompat
import javax.inject.Inject

class PrefChildFragment : DaggerPreferenceFragmentCompat() {
  companion object {
    @JvmStatic
    fun newInstance() = PrefChildFragment()
  }

  @Inject
  lateinit var database: Database

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.preferences)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setup()
  }

  private fun setup() {
    findPreference("key_reset_database").setOnPreferenceClickListener {
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

    findPreference("key_version")
        .summary = BuildConfig.VERSION_NAME
  }

  private fun toast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
  }
}