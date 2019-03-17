package stream.reconfig.kirinmaru.android.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import commons.android.dagger.ApplicationContext
import commons.android.dagger.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import stream.reconfig.kirinmaru.android.prefs.CookiePref

@Module
class NetworkModule {

  @ApplicationScope
  @Provides
  fun okHttp(cookieJar: CookieJar): OkHttpClient = OkHttpClient.Builder()
    .cookieJar(cookieJar)
    .build()

  @ApplicationScope
  @Provides
  fun gson(): Gson = GsonBuilder().create()

  @ApplicationScope
  @Provides
  fun cookieManager(
    @ApplicationContext application: Context,
    gson: Gson,
    cookiePref: CookiePref
  ): CookieJar = CookieManager(application, gson, cookiePref)
}

