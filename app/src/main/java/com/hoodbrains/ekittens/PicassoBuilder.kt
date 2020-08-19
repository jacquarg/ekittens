package com.hoodbrains.ekittens

import android.content.Context
import android.util.Log
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private const val IMAGE_CACHE_SIZE: Long = 10 * 1024 * 1024
private const val IMAGE_CACHE_MAX_AGE = 60 * 60 * 24 * 365

fun initPicassoWithDiskCache(context: Context) {

    val httpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val builder: Response.Builder = response.newBuilder()
                .removeHeader("Cache-Control")
                .addHeader("Cache-Control", "public, max-stale=${IMAGE_CACHE_MAX_AGE}")
            builder.build()
        })
        .cache(Cache(context.getCacheDir(), IMAGE_CACHE_SIZE))
        .build()


    val imageBuilder = Picasso.Builder(context.applicationContext)
        .loggingEnabled(true)
        .downloader(OkHttp3Downloader(httpClient))
        .listener { _, uri, exception ->
            // --> displayed
            Log.e("toto", "Failed to load image: ${uri.path} because of ${exception.message}")
        }

    Picasso.setSingletonInstance(imageBuilder.build())
}
