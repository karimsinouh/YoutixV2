package com.karimsinouh.youtixv2.api

import com.karimsinouh.youtixv2.data.ResponsePage
import com.karimsinouh.youtixv2.data.items.PlaylistItem
import com.karimsinouh.youtixv2.data.items.SearchItem
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.utils.API_KEY
import com.karimsinouh.youtixv2.utils.CHANNEL_ID
import com.karimsinouh.youtixv2.utils.PLAYLIST_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("playlistItems?part=snippet&playlistId=$PLAYLIST_ID&maxResults=20&key=$API_KEY")
    suspend fun getVideos( @Query("pageToken") pageToken:String?="" ):Response<ResponsePage<VideoItem>>

    @GET("playlists?part=snippet,contentDetails&channelId=$CHANNEL_ID&maxResults=20&key=$API_KEY")
    suspend fun getPlaylists( @Query("pageToken") pageToken:String?="" ):Response<ResponsePage<PlaylistItem>>

    @GET("search?part=snippet&channelId=$CHANNEL_ID&key=$API_KEY")
    suspend fun search( @Query("q") q:String ):Response<ResponsePage<SearchItem>>

    @GET("videos?part=snippet&key=$API_KEY")
    suspend fun getSelectedVideos( @Query("id") ids:String ):Response<ResponsePage<VideoItem>>

    @GET("videos?part=snippet,contentDetails,statistics&key=$API_KEY")
    suspend fun getVideo( @Query("id") id:String ):Response<ResponsePage<VideoItem>>

    @GET("playlistItems?part=snippet,contentDetails&key=$API_KEY")
    suspend fun getPlaylistVideos( @Query("playlistId") id:String ):Response<ResponsePage<VideoItem>>

}