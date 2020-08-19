package com.hoodbrains.ekittens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.hoodbrains.ekittens.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.view = this
    }


    fun onClickNewKitten(view: View) {
        val picasso = Picasso.get()
        picasso.isLoggingEnabled = true
        picasso.setIndicatorsEnabled(true)

        picasso
            .load("https://placekitten.com/200/300/")
            .placeholder(R.drawable.ic_launcher_foreground)
            .resize(300, 300)
            .centerInside()
            .into(binding.placeholder)
    }
}

