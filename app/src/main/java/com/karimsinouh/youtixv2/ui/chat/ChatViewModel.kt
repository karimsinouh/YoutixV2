package com.karimsinouh.youtixv2.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karimsinouh.youtixv2.data.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo:ChatRepository
):ViewModel() {

    private val _messages=MutableLiveData<List<Message>?>()
    private val _error=MutableLiveData<String?>()

    val messages:LiveData<List<Message>?> = _messages
    val error:LiveData<String?> = _error

    fun loadMessages(){
        if (_messages.value==null){
            repo.getMessages {
                if (it.isSuccessful)
                    _messages.value=it.data ?: emptyList()
                else
                    _error.value=it.message
            }
        }
    }

    fun sendMessage(text:String){
        repo.sendMessage(text){
            if (!it.isSuccessful){
                _error.value=it.message
            }
        }
    }
}