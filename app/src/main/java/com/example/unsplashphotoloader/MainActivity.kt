package com.example.unsplashphotoloader

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashphotoloader.RetrofitFactory.Companion.photoService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewCollection: RecyclerView
    private lateinit var recyclerViewPhoto: RecyclerView
    private lateinit var collectionsAdapter: CollectionsAdapter
    private lateinit var photoAdapter: PhotoAdapter

    var collection: ArrayList<Collection> = ArrayList()
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerViewCollection = findViewById(R.id.recyclerview_collection)
        recyclerViewPhoto = findViewById(R.id.recyclerview_photo)
        recyclerViewPhoto.setHasFixedSize(true)
        recyclerViewCollection.layoutManager = layoutManager
        recyclerViewPhoto.layoutManager = GridLayoutManager(this, 1)
        recyclerViewCollection.addItemDecoration(DividerItemDecoration(recyclerViewCollection.context, 0))
        loadCollections(page)
        parseJson()
        setupAdapter()

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                this@MainActivity.page += 1
                LoadMoreData(page)
            }
        })
        recyclerViewCollection.addOnScrollListener(scrollListener)

    }

    private fun setupAdapter(){
        collectionsAdapter = CollectionsAdapter(this@MainActivity)
        photoAdapter = PhotoAdapter(this@MainActivity)
        recyclerViewCollection.adapter = collectionsAdapter
        recyclerViewPhoto.adapter = photoAdapter
    }

    fun loadCollections(page:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val responseCollection = photoService.getCollections(page)

            withContext(Dispatchers.Main) {
                if (responseCollection.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(responseCollection.body())
                    Log.d("Pretty Printed JSON :", prettyJson)

                    val items = responseCollection.body()
                    if (items != null) {

                        for (i in 0 until items.count()) {

                            val id = items[i].id ?: "N/A"
                            val title = items[i].title ?: "N/A"
                            Log.d("title", title)

                            val modelCollection = Collection(id, title)

                            collection.add(modelCollection)

                            recyclerViewCollection.visibility = View.VISIBLE
                            collectionsAdapter.setCollection(collection)
                        }

                    } else {
                    }
                } else {
                    Log.e("RETROFIT ERROR", responseCollection.code().toString())
                }
            }
        }
    }

    fun LoadMoreData(page: Int){

        Handler().postDelayed({

            loadCollections(page)
            scrollListener.setLoaded()
            recyclerViewCollection.post {
                collectionsAdapter.notifyDataSetChanged()
            }
        }, 1000)

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        var searchItem: MenuItem = menu!!.findItem(R.id.search)
        var searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchPhoto(query)
                return true
            }
            
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("LongLogTag")
    fun parseJson(){

        CoroutineScope(Dispatchers.IO).launch {
            val responsePhoto =  photoService.getRandomPhoto()

            withContext(Dispatchers.Main){
                if (responsePhoto.isSuccessful){

                    val width = responsePhoto.body()?.width ?: "N/A"
                    Log.d("width", width)
                    val height = responsePhoto.body()?.height?:"N/A"
                    Log.d("height", height)
                    val description = responsePhoto.body()?.description?:""
                    Log.d("description", description)
                    val imageUrl = responsePhoto.body()?.urls?.full?:"N/A"
                    Log.d("imageUrl", imageUrl)

                    val model = Photo(
                        width,
                        height,
                        description,
                        imageUrl
                    )
                    var photo: ArrayList<Photo> = ArrayList()
                    photo.add(model)
                    photoAdapter.setPhoto(photo)

                }else{
                    Log.e("RETROFIT ERROR", responsePhoto.code().toString())
                }
            }

        }

    }

    fun searchPhoto(query:String){

        CoroutineScope(Dispatchers.IO).launch {

            val responseSearchPhoto = photoService.searchPhoto(query)

            withContext(Dispatchers.Main){
                if(responseSearchPhoto.isSuccessful){


                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val Json = gson.toJson(responseSearchPhoto.body())
                    Log.d("Printed JSON :", Json)

                    val items = responseSearchPhoto.body()?.results
                    if(items != null){
                        var collectionPhoto: ArrayList<Photo> = ArrayList()
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
                    Log.e("RETROFIT ERROR", responseSearchPhoto.code().toString())

                }
            }
        }

    }





}
