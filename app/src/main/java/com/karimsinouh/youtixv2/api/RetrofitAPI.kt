package com.karimsinouh.youtixv2.api

import com.karimsinouh.youtixv2.utils.API_KEY
import com.karimsinouh.youtixv2.utils.PLAYLIST_ID
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitAPI {

    //just for test
    @GET("playlistItems?part=snippet,contentDetails&playlistId=$PLAYLIST_ID&maxResults=20&key=$API_KEY")
    fun getVideos():Response<String>

}