package com.karimsinouh.youtixv2.data

import com.karimsinouh.youtixv2.data.thumbnails.Thumbnails
import java.io.Serializable

data class Snippet(
    val publishedAt:String,
    val title:String,
    val description:String,
    val thumbnails:Thumbnails,
    val playlistId:String?=null,
    val position:Int?=null,
    val resourceId: ResourceId?=null
):Serializable