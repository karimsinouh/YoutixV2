package com.karimsinouh.youtixv2.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.database.Database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
        private val db:Database,
        private val repo:Repository
):ViewModel() {


    //mutable values
    private val _list = MutableLiveData<List<VideoItem>>()
    private val _error=MutableLiveData<String>()


    //observable values
    val history by lazy {
        db.history().list()
    }

    val watchLater by lazy {
        db.watchLater().list()
    }

    val error:LiveData<String> = _error
    val list:LiveData<List<VideoItem>> =_list



    fun loadList(ids:String)=viewModelScope.launch{
        repo.getSelectedVideos(ids){
            if (it.isSuccessful)
                _list.postValue(it.data)
            else
                _error.postValue(it.message)
        }
    }

}