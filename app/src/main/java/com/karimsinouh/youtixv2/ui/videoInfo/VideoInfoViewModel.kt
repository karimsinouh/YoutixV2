package com.karimsinouh.youtixv2.ui.videoInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.entities.WatchLater
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.database.Database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoInfoViewModel @Inject constructor(
    private val repo:Repository,
) :ViewModel() {


    //mutable values
    private val _error=MutableLiveData<String>()
    private val _video=MutableLiveData<VideoItem>()

    //observable values
    val video:LiveData<VideoItem> =_video
    val error:LiveData<String> =_error

    suspend fun loadVideo(id:String){
        repo.getVideo(id){
            if (it.isSuccessful){
                _video.postValue(it.data)
            }else{
                _error.postValue(it.message)
            }
        }
    }

    fun addToWatchLater()=viewModelScope.launch{
        val item=WatchLater(video.value?.id!!)
        repo.db.watchLater().add(item)
    }

    fun deleteFromWatchLater()=viewModelScope.launch{
        repo.db.watchLater().delete(video.value?.id!!)
    }

    fun exists(result:(Boolean)->Unit)=
            viewModelScope.launch {
                result(repo.db.watchLater().exists(video.value?.id!!))
            }

}