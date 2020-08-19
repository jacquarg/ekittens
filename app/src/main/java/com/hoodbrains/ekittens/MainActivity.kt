package com.hoodbrains.ekittens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hoodbrains.ekittens.databinding.ActivityMainBinding

import java.io.IOException
import com.squareup.picasso.*
import com.squareup.picasso.Callback
import okhttp3.*
import okhttp3.Cache
import okhttp3.Request
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


//    lateinit var picasso: Picasso


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.view = this

        initPicassoWithDiskCache(this)
//
//
//        val interceptor: Interceptor = object : Interceptor {
//            @Throws(IOException::class)
//            override fun intercept(chain: Interceptor.Chain): Response? {
////                var request: Request = chain.request()
////                val builder: Request.Builder =
////                    request.newBuilder().addHeader("Cache-Control", "max-age=${60 * 60 * 24 * 365}")
////                request = builder.build()
////                return chain.proceed(request)
//
//                val response = chain.proceed(chain.request())
//                Log.e("response header", response.header("Cache-Control"))
//                val builder: Response.Builder = response.newBuilder()
//                    .removeHeader("Cache-Control")
//                    .addHeader("Cache-Control", "public, max-stale=${60 * 60 * 24 * 365}")
//
//                return builder.build()
//
//            }
//        }
//
//
//        val provideOfflineCacheInterceptor: Interceptor = object: Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response? {
//                try {
//                    return chain.proceed(chain.request())
//                } catch (e: Exception) {
//                    Log.e("provideOfflineCache", "toto")
//                    val cacheControl = CacheControl.Builder()
//                            .onlyIfCached()
//                            .maxStale(1, TimeUnit.DAYS)
//                            .build();
//
//                        val offlineRequest = chain.request().newBuilder()
//                            .cacheControl(cacheControl)
//                            .build();
//                        return chain.proceed(offlineRequest);
//                    }
//                }
//            }
//
//        val mClient = OkHttpClient.Builder()
//            .addNetworkInterceptor(interceptor)
////            .addInterceptor(provideOfflineCacheInterceptor)
//            .cache(Cache(getCacheDir(), 10 * 1024 * 1024))
//            .build()
//
//
//        val imageBuilder = Picasso.Builder(this.applicationContext)
//            .loggingEnabled(true)
//            .downloader(OkHttp3Downloader(mClient))
//            .listener { _, uri, exception ->
//                // --> displayed
//                Log.e("toto", "Failed to load image: ${uri.path} because of ${exception.message}")
//            }
//
//            picasso = imageBuilder.build()
////        picasso = Picasso.get()
////
////        try {
////            Picasso.setSingletonInstance(picasso)
////        } catch (exception: Exception) {
////            // Picasso instance was already set
////            Log.e(TAG, exception.message)
////        }
//
//        picasso.isLoggingEnabled = true
//        picasso.setIndicatorsEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        loadFromCache()
    }


    private fun loadFromCache() {
        Picasso.get().load("https://picsum.photos/200/300")
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .networkPolicy(NetworkPolicy.OFFLINE)
//            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.placeholder)
    }

    private fun loadFromNetwork() {
        Picasso.get().load("https://picsum.photos/200/300")
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .into(binding.placeholder, object: Callback {
                override fun onSuccess() {
                }

                override fun onError(e: Exception?) {
                    // not displayed...
                    Log.e("loadnetwork", "error")
                  //  loadFromCache()
                }
            })
    }

    fun onClickNewKitten(view: View) {
        loadFromNetwork()
//
//        picasso
//            .load("https://picsum.photos/200/300")
////            .load("https://placekitten.com/200/300/")
//            .memoryPolicy(MemoryPolicy.NO_CACHE)
//            .networkPolicy(NetworkPolicy.NO_STORE, NetworkPolicy.NO_CACHE)
//            //.placeholder(R.drawable.ic_launcher_foreground)
////            .resize(300, 300)
////            .centerInside()
//            .into(binding.placeholder)
    }
}

