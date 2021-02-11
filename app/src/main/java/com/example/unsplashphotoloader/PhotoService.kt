package com.example.unsplashphotoloader

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoService {

    companion object{
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/photos/random")
    suspend fun  getRandomPhoto(): Response<PhotoApi>

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/collections")
    suspend fun getCollections(@Query("page") page:Int): Response<List<CollectionsApi>>

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/collections/{id}/photos")
    suspend fun getCollectionsPhoto(
        @Path("id") id: Int,
        @Query("page") page:Int):Response<List<PhotoApi>>

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/search/photos")
    suspend fun searchPhoto(@Query("query") query: String) :Response<Results>
}