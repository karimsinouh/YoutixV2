package com.karimsinouh.youtixv2.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.entities.HistoryItem
import com.karimsinouh.youtixv2.data.entities.WatchLater
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.database.Database
import com.karimsinouh.youtixv2.utils.ACTION_HISTORY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.FieldPosition
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
        private val repo:Repository
):ViewModel() {


    //mutable values
    private val _list = MutableLiveData<List<VideoItem>>()
    private val _error=MutableLiveData<String>()
    private val _selectedItems=MutableLiveData<List<String>>(emptyList())




    //observable values
    val history by lazy {
        repo.db.history().list()
    }

    val watchLater by lazy {
        repo.db.watchLater().list()
    }

    val error:LiveData<String> = _error
    val list:LiveData<List<VideoItem>> =_list
    val selectedItems:LiveData<List<String>> = _selectedItems

    fun selectItem(id:String){

        val newList=ArrayList(_selectedItems.value)

        if (selectedItems.value?.contains(id)!!){
            newList.remove(id)
        }else{
            newList.add(id)
        }
        _selectedItems.value=newList
    }

    fun deleteAllSelected(action:String)=viewModelScope.launch{
        if (action== ACTION_HISTORY)
            repo.db.history().deleteByIds(selectedItems.value!!)
        else
            repo.db.watchLater().deleteByIds(selectedItems.value!!)
        clearSelected()
    }

    fun clearSelected() {
        _selectedItems.value= emptyList()
    }

    fun loadList(ids:String)=viewModelScope.launch{
        repo.getSelectedVideos(ids){
            if (it.isSuccessful)
                _list.postValue(it.data)
            else
                _error.postValue(it.message)
        }
    }

    fun deleteFromHistory(position: Int)=viewModelScope.launch {
        val item=list.value!![position]
        repo.db.history().delete(item.id!!)
    }

    fun deleteFromWatchLater(position: Int)=viewModelScope.launch {
        val item=list.value!![position]
        repo.db.watchLater().delete(item.id!!)
    }


}