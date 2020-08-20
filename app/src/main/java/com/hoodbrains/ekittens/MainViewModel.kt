package com.hoodbrains.ekittens

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.picasso.Picasso
import java.lang.Exception

/**
 * ViewModel for the main activity.
 * Manage image source url, from network/ from cache choice, and error handling.
 */
class MainViewModel: ViewModel(), Picasso.Listener {
    private var width = 360
    private var height = 600

    val error = MutableLiveData<Int>()
    val loadFromNetwork = MutableLiveData<Boolean>()

    // Kittens are always the same : /
//    val imageUrl get() = "https://placekitten.com/$width/$height"
    val imageUrl get() = "https://picsum.photos/$width/$height"

    /**
     * Set the actual screen size.
     */
    fun updateScreenSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun onClickNewKitten(view: View) {
        loadFromNetwork.postValue(true)
    }

    fun onResumeActivity() {
        loadFromNetwork.postValue(false)
    }

    /**
     * WHen image load failed, ask to display a specific error message.
     */
    override fun onImageLoadFailed(picasso: Picasso?, uri: Uri?, exception: Exception?) {
        if (exception != null && loadFromNetwork.value == true) {
            error.postValue(R.string.network_error)
        }
    }
}