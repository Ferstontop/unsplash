package com.example.unsplashphotoloader

import kotlinx.serialization.Serializable

data class Results(

    var results: List<PhotoApi>?
)

data class PhotoApi(

    var width:String?,
    var height:String?,
    var description:String?,
    var urls: Urls?,
)

data class Urls(
    var full: String?
)

data class CollectionsApi (

    var id:String?,
    var title:String?

)
