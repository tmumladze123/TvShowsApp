package com.example.movieapplication.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImage(url:String?) {
    Glide.with(context).load(url).into(this)
}