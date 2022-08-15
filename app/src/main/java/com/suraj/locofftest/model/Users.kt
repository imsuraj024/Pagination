package com.suraj.locofftest.model

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

data class Users(
 val id: Int? = null,
 val email: String? = null,
 val first_name: String? = null,
 val last_name: String? = null,
 val avatar: String? = null
)

@BindingAdapter("app:imageUser" , "app:progressUser")
fun setImageMovie(image: ImageView, imageUrl: String?, progressMovie : ProgressBar) {

 Glide.with(image.context)
  .load(imageUrl)
  .listener(object: RequestListener<Drawable> {
   override fun onLoadFailed(
    e: GlideException?,
    model: Any?,
    target: Target<Drawable>?,
    isFirstResource: Boolean
   ): Boolean {
    progressMovie.visibility = View.GONE
    return false
   }

   override fun onResourceReady(
    resource: Drawable?,
    model: Any?,
    target: Target<Drawable>?,
    dataSource: DataSource?,
    isFirstResource: Boolean
   ): Boolean {
    progressMovie.visibility = View.GONE
    return false
   }

  })
  .diskCacheStrategy(DiskCacheStrategy.ALL)
  .centerCrop()
  .into(image)

}

@BindingAdapter("app:firstName" ,  "app:lastName")
fun setUserName(year : TextView, first_name: String, last_name : String ) {
 year.text = first_name + " " + last_name
}

@BindingAdapter("app:firstName", "app:userId")
fun setUserDesc(textView: TextView, first_name: String,id: Int?){
 textView.text = "The user id of "+first_name+" is "+id
}
