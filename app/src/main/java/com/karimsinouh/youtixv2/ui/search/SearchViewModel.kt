package com.karimsinouh.youtixv2.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.items.SearchItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo:Repository):ViewModel() {


    private val _result=MutableLiveData<List<SearchItem>>()
    private val _searchHistory=MutableLiveData<List<String>>()
    private val _error=MutableLiveData<String>()

    val result:LiveData<List<SearchItem>> =_result
    val searchHistory:LiveData<List<String>> = _searchHistory
    val error:LiveData<String> = _error

    suspend fun search(q:String){
        repo.search(q){
            if (it.isSuccessful){
                _result.postValue(it.data?.items!!)
            }else{
                _error.postValue(it.message)
            }
        }
    }

}