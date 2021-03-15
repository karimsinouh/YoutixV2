package com.karimsinouh.youtixv2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.items.PlaylistIem
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
    private val _playlists = MutableLiveData(mutableListOf<PlaylistIem>())
    private val _error = MutableLiveData<String>()

    //observable live data
    val videos:LiveData<MutableList<VideoItem>> =_videos
    val playlists:LiveData<MutableList<PlaylistIem>> = _playlists
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
            _playlists.postValue((it.data?.items ?: emptyList()) as MutableList<PlaylistIem>?)
        }

    }

}