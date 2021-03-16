package com.karimsinouh.youtixv2.api

import com.karimsinouh.youtixv2.data.ResponsePage
import com.karimsinouh.youtixv2.data.Result
import com.karimsinouh.youtixv2.data.items.PlaylistItem
import com.karimsinouh.youtixv2.data.items.SearchItem
import com.karimsinouh.youtixv2.data.items.VideoItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val api:RetrofitAPI) {

    suspend fun getVideo(id:String, listener:(Result<VideoItem>)->Unit ){
        api.getVideo(id).apply {
            listener(
                    if (isSuccessful)
                        if (body()?.items?.isNotEmpty()!!)
                            Result(true,body()?.items!![0],message())
                        else
                            Result(false,null,"This video is not available")
                    else
                        Result(false,null,message())

            )
        }
    }

    suspend fun getVideos(pageToken:String?, listener:(Result<ResponsePage<VideoItem>>)->Unit){
        api.getVideos(pageToken).apply {
            listener(Result(isSuccessful,body(),message()))
        }
    }

    suspend fun getPlaylists(pageToken:String, listener:(Result<ResponsePage<PlaylistItem>>)->Unit){
        api.getPlaylists(pageToken).apply {
            listener(Result(isSuccessful,body(),message()))
        }
    }

    suspend fun getPlaylistVideos(playlistId:String,listener:(Result<List<VideoItem>>)->Unit){
        api.getPlaylistVideos(playlistId).apply {
            listener(
                    if (isSuccessful)
                        if (body()?.items?.isNotEmpty()!!)
                            Result(true,body()?.items,null)
                        else
                            Result(false,null,"This playlist doesn't have any videos")
                    else
                        Result(false,null,message())
            )
        }
    }

    suspend fun search(q:String,listener:(Result<ResponsePage<SearchItem>>)->Unit){
        api.search(q).apply {
            listener(Result(isSuccessful,body(),message()))
        }
    }

}