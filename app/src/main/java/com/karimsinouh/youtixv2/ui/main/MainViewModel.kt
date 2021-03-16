package com.karimsinouh.youtixv2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.items.PlaylistItem
import com.karimsinouh.youtixv2.data.items.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo:Repository):ViewModel() {

    //page tokens
    private var videosNextPageToken=""
    private var playlistsNextPageToken=""

    //mutable live data
    private val _videos = MutableLiveData(mutableListOf<VideoItem>())
    private val _playlists = MutableLiveData(mutableListOf<PlaylistItem>())
    private val _error = MutableLiveData<String>()

    //observable live data
    val videos:LiveData<MutableList<VideoItem>> =_videos
    val playlists:LiveData<MutableList<PlaylistItem>> = _playlists
    val error:LiveData<String> =_error


    suspend fun loadVideos()=
            repo.getVideos(videosNextPageToken){
                if (it.isSuccessful)
                    _videos.value?.let {value->
                        value.addAll(it.data?.items ?: emptyList())
                        _videos.postValue(value)
                        videosNextPageToken=it.data?.nextPageToken ?: ""
                    }
                else
                    _error.postValue(it.message)
            }

    suspend fun loadPlaylists(){

        repo.getPlaylists(playlistsNextPageToken){

            if(it.isSuccessful){
                _playlists.value?.let { value->
                    value.addAll(it.data?.items ?: emptyList())
                    _playlists.postValue(value)
                    playlistsNextPageToken=it.data?.nextPageToken ?: ""
                }
            }else{
                _error.postValue(it.message)
            }

        }

    }

}