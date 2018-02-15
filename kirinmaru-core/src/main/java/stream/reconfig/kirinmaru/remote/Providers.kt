package stream.reconfig.kirinmaru.remote

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Temporary DI providers
 */
object Providers {
  fun retrofitBuilder(gson: Gson? = null): Retrofit.Builder {
    val gsonConverterFactory = gson?.let { GsonConverterFactory.create(it) }
        ?: GsonConverterFactory.create()
    return Retrofit.Builder()
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  }
}