package com.karimsinouh.youtixv2.api

import com.karimsinouh.youtixv2.data.ResponsePage
import com.karimsinouh.youtixv2.data.Result
import com.karimsinouh.youtixv2.data.items.PlaylistIem
import com.karimsinouh.youtixv2.data.items.VideoItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val api:RetrofitAPI) {

    suspend fun getVideos(pageToken:String?, listener:(Result<ResponsePage<VideoItem>>)->Unit){
        val response=api.getVideos(pageToken)
        response.apply {
            listener(Result(isSuccessful,body(),message()))
        }
    }

    suspend fun getPlaylists(pageToken:String, listener:(Result<ResponsePage<PlaylistIem>>)->Unit){
        val response=api.getPlaylists(pageToken)
        response.apply {
            listener(Result(isSuccessful,body(),message()))
        }
    }

}