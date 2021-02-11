package com.example.unsplashphotoloader

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashphotoloader.RetrofitFactory.Companion.photoService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.Collection

class CollectionPhotoActivity:AppCompatActivity() {

    private  var collectionId:Int = 0
    private lateinit var recyclerViewPhoto: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter

    var collectionPhoto: ArrayList<Photo> = ArrayList()
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var loadMoreItemsCollectionPhoto: ArrayList<Photo>
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerViewPhoto = findViewById(R.id.recyclerview_photo)
        recyclerViewPhoto.setHasFixedSize(true)
        recyclerViewPhoto.layoutManager = layoutManager
        photoAdapter = PhotoAdapter(this@CollectionPhotoActivity)
        recyclerViewPhoto.adapter = photoAdapter

        var intent:Intent = getIntent()
        collectionId = intent.getIntExtra("id",1)
        Log.d("collectionId", collectionId.toString())

        parseJson(page)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                this@CollectionPhotoActivity.page += 1
                LoadMoreData(page)
            }
        })
        recyclerViewPhoto.addOnScrollListener(scrollListener)

    }

    override fun onStart() {
        super.onStart()

    }

    @SuppressLint("LongLogTag")
    fun parseJson(page:Int){

        CoroutineScope(Dispatchers.IO).launch {

            val responseCollectionPhoto = photoService.getCollectionsPhoto(collectionId,page)

            withContext(Dispatchers.Main){
                if(responseCollectionPhoto.isSuccessful){

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val Json = gson.toJson(responseCollectionPhoto.body())
                    Log.d("Printed JSON :", Json)

                    val items = responseCollectionPhoto.body()
                    if(items != null){

                        for (i in 0 until items.count()) {

                            val width = items[i].width ?: "N/A"
                            Log.d("width", width)
                            val height = items[i].height?:"N/A"
                            Log.d("height", height)
                            val description = items[i].description?:""
                            Log.d("description", description)
                            val imageUrl = items[i].urls?.full?:"N/A"
                            Log.d("imageUrl", imageUrl)

                            val model = Photo(
                                width,
                                height,
                                description,
                                imageUrl
                            )

                            collectionPhoto.add(model)
                            recyclerViewPhoto.visibility = View.VISIBLE
                            photoAdapter.setPhoto(collectionPhoto)

                        }
                    }else{}

                }else{
                    Log.e("RETROFIT ERROR", responseCollectionPhoto.code().toString())

                }
            }
        }

    }

    fun LoadMoreData(page: Int){
        Handler().postDelayed({
            parseJson(page)
            scrollListener.setLoaded()
            recyclerViewPhoto.post {
                photoAdapter.notifyDataSetChanged()
            }
        }, 1000)
    }
}