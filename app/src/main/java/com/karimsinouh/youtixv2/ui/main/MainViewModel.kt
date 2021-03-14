package com.karimsinouh.youtixv2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.youtixv2.api.RetrofitAPI
import com.karimsinouh.youtixv2.data.items.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val api:RetrofitAPI):ViewModel() {

    //page tokens
    private var videosNextPageToken=""
    private var playlistsNextPageToken=""

    //mutable live data
    private val _videos = MutableLiveData(mutableListOf<VideoItem>())
    private val _error = MutableLiveData<String>()

    //observable live data
    val videos:LiveData<MutableList<VideoItem>> =_videos
    val error:LiveData<String> =_error


    suspend fun loadVideos(){
        val response=api.getVideos(videosNextPageToken)
        videosNextPageToken=response.body()?.nextPageToken ?: ""

        if (response.isSuccessful)
            _videos.value?.let {
                it.addAll(response.body()?.items?: emptyList())
                _videos.postValue(it)
            }
        else
            _error.postValue(response.message())
    }


}