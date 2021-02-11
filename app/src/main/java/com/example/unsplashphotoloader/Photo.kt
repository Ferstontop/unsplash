package com.example.unsplashphotoloader

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(var width:String,var height: String,var description: String,var imageUrl:String):Parcelable{}

@Parcelize
data class Collection(var id:Int,var title:String):Parcelable{}


