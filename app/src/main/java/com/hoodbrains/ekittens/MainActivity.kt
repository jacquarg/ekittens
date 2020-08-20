package com.hoodbrains.ekittens

import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hoodbrains.ekittens.databinding.ActivityMainBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception

/**
 * Main activity of the application, which display the images on demand.
 */
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    lateinit var picasso: Picasso


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        picasso = initPicassoWithDiskCache(this, viewModel)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.placeholder.getViewTreeObserver().addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewModel.updateScreenSize(binding.placeholder.measuredWidth, binding.placeholder.measuredHeight)
                binding.placeholder.getViewTreeObserver().removeOnGlobalLayoutListener(this)
            }
        })


        viewModel.error.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                showSnackbar(errorMessage)
                viewModel.error.postValue(null)
            }
        })

        viewModel.loadFromNetwork.observe(this, Observer { fromNetwork ->
            if (fromNetwork != null) {
                loadImage(fromNetwork)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.onResumeActivity()
    }

    /**
     * Display an error message through a snackbar.
     * @param message the resourceId of the message to display.
     */
    private fun showSnackbar(message: Int) {
        val snackbar = Snackbar.make(binding.container, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    /**
     * Load an image and display it in the placeholder. If [fromNetwork] is true, fetch it from
     * network, else, fetch it from the disk cache.
     */
    private fun loadImage(fromNetwork: Boolean) {
        picasso.load(viewModel.imageUrl)
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .networkPolicy(if (fromNetwork) NetworkPolicy.NO_CACHE else NetworkPolicy.OFFLINE)
            .into(binding.placeholder, object: Callback {
                override fun onSuccess() {
                    viewModel.loadFromNetwork.postValue(null)
                }

                override fun onError(e: Exception?) {
                    viewModel.loadFromNetwork.postValue(null)
                }

            })
    }
}
