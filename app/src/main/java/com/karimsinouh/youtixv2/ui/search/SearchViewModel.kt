package com.karimsinouh.youtixv2.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.entities.SearchHistory
import com.karimsinouh.youtixv2.data.items.SearchItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo:Repository):ViewModel() {

    var nextPageToken=""
    var query:String=""
    var searched=false

    private val _result=MutableLiveData<List<SearchItem>>(emptyList())
    private val _error=MutableLiveData<String>()

    val result:LiveData<List<SearchItem>> =_result
    val error:LiveData<String> = _error
    var searchHistory=repo.db.searchHistory().list()

    fun search(q:String,loadMore:Boolean?=false)=viewModelScope.launch{
        if (q.isEmpty())
            return@launch

        searched=true

        query=q

        if(!loadMore!!)
            nextPageToken=""

        addSearchHistory(q)

        repo.search(q,nextPageToken){
            if (it.isSuccessful){

                val value= mutableListOf<SearchItem>()
                if(loadMore)
                    value.addAll(result.value ?: emptyList())
                value.addAll(it.data?.items ?: emptyList())
                _result.postValue(value)
                nextPageToken=it.data?.nextPageToken ?: ""
            }else
                _error.postValue(it.message)

        }
    }

    fun searchInHistory(q:String,listener: (List<SearchHistory>) -> Unit)=viewModelScope.launch{
        listener(repo.db.searchHistory().search("%$q%"))
    }

    private suspend fun addSearchHistory(q:String){
        val searchItem=SearchHistory(q)
        if (!repo.db.searchHistory().exists(q))
            repo.db.searchHistory().add(searchItem)
    }

    fun removeSearchHistory(item:SearchHistory)=viewModelScope.launch{
        repo.db.searchHistory().delete(item)
    }

}