package com.mine.growthstory.utils

import com.bumptech.glide.request.RequestOptions
import com.mine.growthstory.R
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.Glide
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.model.GlideUrl
import android.text.TextUtils
import android.widget.ImageView

object GlideUtil {
    fun showImage(imageView: ImageView?, url: String?) {
        val options = RequestOptions()
            .placeholder(R.drawable.vd_album_default)
            .error(R.drawable.vd_album_default)
            .centerCrop()
            .skipMemoryCache(true) //跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(imageView!!).load(url).apply(options).into(imageView)
    }

    fun showImage(imageView: ImageView?, @DrawableRes resId: Int?) {
        val options = RequestOptions()
            .placeholder(R.drawable.vd_album_default)
            .error(R.drawable.vd_album_default)
            .centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(imageView!!).load(resId).apply(options).into(imageView)
    }

    fun showImage(
        imageView: ImageView?,
        headers: Map<String?, String?>,
        url: String?,
        defaultResId: Int
    ) {
        val builder = LazyHeaders.Builder()
        for ((key, value) in headers) {
            builder.addHeader(key!!, value!!)
        }
        val options = RequestOptions()
            .placeholder(defaultResId)
            .error(defaultResId)
        var glideUrl: GlideUrl? = null
        if (!TextUtils.isEmpty(url)) {
            glideUrl = GlideUrl(url, builder.build())
        }
        Glide.with(imageView!!)
            .load(glideUrl)
            .apply(options)
            .into(imageView)
    }
}