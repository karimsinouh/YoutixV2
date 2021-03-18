package com.karimsinouh.youtixv2.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.items.SearchItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo:Repository):ViewModel() {

    var nextPageToken=""
    var query:String=""

    private val _result=MutableLiveData<List<SearchItem>>()
    private val _error=MutableLiveData<String>()

    val result:LiveData<List<SearchItem>> =_result
    val error:LiveData<String> = _error

    fun search(q:String,loadMore:Boolean?=false)=viewModelScope.launch{
        query=q
        repo.search(q,nextPageToken){
            if (it.isSuccessful){

                val value= mutableListOf<SearchItem>()
                if(loadMore!!)
                    value.addAll(result.value ?: emptyList())
                value.addAll(it.data?.items ?: emptyList())
                _result.postValue(value)
                nextPageToken=it.data?.nextPageToken ?: ""
            }else
                _error.postValue(it.message)

        }
    }

}