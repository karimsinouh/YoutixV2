package com.karimsinouh.youtixv2.ui.viewPlaylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.entities.HistoryItem
import com.karimsinouh.youtixv2.data.entities.WatchLater
import com.karimsinouh.youtixv2.data.items.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewPlaylistViewModel @Inject constructor( private val repo:Repository) : ViewModel() {

    var nextPageToken=""

    //mutable values
    private val _videos=MutableLiveData(mutableListOf<VideoItem>())
    private val _video=MutableLiveData<VideoItem>()
    private val _error=MutableLiveData<String>()

    //observable values
    val videos:LiveData<MutableList<VideoItem>> =_videos
    val video:LiveData<VideoItem> =_video
    val error:LiveData<String> =_error

    suspend fun loadVideos(playlistId:String){
        if(videos.value?.isNotEmpty()!! && nextPageToken!="" || videos.value?.isEmpty()!! && nextPageToken=="" )
            repo.getPlaylistVideos(playlistId,nextPageToken){
                if (it.isSuccessful) {

                    val value= mutableListOf<VideoItem>()
                    value.addAll(videos.value ?: emptyList())
                    value.addAll(it.data?.items ?: emptyList())

                    _videos.postValue(value)

                    Log.d("wtf",value.size.toString())

                    nextPageToken=it.data?.nextPageToken ?: ""
                }
                else
                    setError(it.message)
        }
    }

    fun loadVideo(videoId:String)=viewModelScope.launch {
        repo.getVideo(videoId){
            if (it.isSuccessful)
                _video.postValue(it.data)
            else
                setError(it.message)
        }
    }

    fun addToWatchLater()=viewModelScope.launch{
        val item= WatchLater(video.value?.id!!)
        repo.db.watchLater().add(item)
    }

    fun deleteFromWatchLater()=viewModelScope.launch{
        repo.db.watchLater().delete(video.value?.id!!)
    }

    fun exists(result:(Boolean)->Unit)= viewModelScope.launch {
                result(repo.db.watchLater().exists(video.value?.id!!))
            }

    fun existsInHistory(result:(exists:Boolean,item: HistoryItem?)->Unit)=viewModelScope.launch{
        val id:String=video.value?.id!!
        if(repo.db.history().exists(id))
            result(true,repo.db.history().get(id))
        else
            result(false,null)
    }

    fun setError(e: String?) {
        _error.postValue(e?:"Something went wrong")
    }

}