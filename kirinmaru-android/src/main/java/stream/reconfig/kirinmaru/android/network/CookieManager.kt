package stream.reconfig.kirinmaru.android.network

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import stream.reconfig.kirinmaru.android.di.qualifiers.ApplicationContext
import stream.reconfig.kirinmaru.android.prefs.CookiePref
import stream.reconfig.kirinmaru.android.util.validator.ThreadValidator
import javax.inject.Inject

private typealias CookieMap = MutableMap<String, MutableList<Cookie>>

/**
 * Cookie Manager which simply override old cookies
 * with new ones for a given domain
 */
class CookieManager @Inject constructor(
    @ApplicationContext private val application: Context,
    private val gson: Gson,
    private val cookiePref: CookiePref
) : CookieJar, LifecycleObserver {
  private val gsonType = object : TypeToken<CookieMap>() {}.type

  private val map: CookieMap by lazy {
    ThreadValidator.validateWorkerThread()
    val json = cookiePref.load()
    gson.fromJson<CookieMap?>(json, gsonType) ?: mutableMapOf()
  }

  override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
    return url?.let {
      map[it.topPrivateDomain()]
    } ?: mutableListOf()
  }

  override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
    url?.topPrivateDomain()?.let { domain ->
      cookies?.let { map.put(domain, it) }
    }
  }

  fun persist() {
    Completable.fromCallable { cookiePref.persist(gson.toJson(map, gsonType)) }
        .subscribeOn(Schedulers.io())
        .subscribe()
  }
}