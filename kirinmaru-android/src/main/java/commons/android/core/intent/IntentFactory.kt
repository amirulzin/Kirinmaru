package commons.android.core.intent

import android.content.Intent
import android.net.Uri

object IntentFactory {
  @JvmStatic
  fun createBrowserIntent(url: String): Intent {
    return Intent(Intent.ACTION_VIEW)
      .apply { data = Uri.parse(url) }
  }
}