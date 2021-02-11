package com.example.unsplashphotoloader

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoActivity:AppCompatActivity() {
    companion object{
        const val EXTRA_PHOTO = "PhotoActivity.EXTRA_PHOTO"
    }

    private lateinit var imageView: ImageView
    private lateinit var photo: Photo
    private lateinit var txtHeight: TextView
    private lateinit var txtWidth:TextView
    private lateinit var txtDescription:TextView
    private lateinit var txtUrl:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        photo = intent.getParcelableExtra(EXTRA_PHOTO)
        imageView = findViewById(R.id.image)
        txtHeight = findViewById(R.id.img_height)
        txtWidth = findViewById(R.id.img_width)
        txtDescription = findViewById(R.id.img_description)
        txtUrl= findViewById(R.id.img_url)

        Picasso.get()
            .load(photo.imageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .fit()
            .priority(Picasso.Priority.HIGH)
            .into(imageView)

        txtHeight.text = photo.height
        txtWidth.text = photo.width
        txtDescription.text = photo.description
        txtUrl.text = photo.imageUrl
    }

    override fun onStart() {
        super.onStart()


    }

}