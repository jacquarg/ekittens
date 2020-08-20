package com.hoodbrains.ekittens

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

/**
 * @property IMAGE_CACHE_SIZE Max cache size allowable for the images on disk (bytes)
 */
private const val IMAGE_CACHE_SIZE: Long = 10 * 1024 * 1024

/**
 * @property IMAGE_CACHE_MAX_AGE Maxage of an image in cache to be displayed to user (seconds)
 */
private const val IMAGE_CACHE_MAX_AGE = 60 * 60 * 24 * 365


/**
 * Build a picasso instance which force cache on disk.
 * @param context a Android context.
 * @param listener to listen for error while fetching an image.
 * @return the picasso instance.
 */
fun initPicassoWithDiskCache(context: Context, listener: Picasso.Listener): Picasso {

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
        .listener(listener)
    return imageBuilder.build()
}
