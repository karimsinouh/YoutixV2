package com.karimsinouh.youtixv2.ui.chat

import android.content.SharedPreferences
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karimsinouh.youtixv2.data.Message
import com.karimsinouh.youtixv2.data.Result
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import javax.inject.Named

@ViewModelScoped
class ChatRepository @Inject constructor(
    @Named("identity_prefs") private val prefs:SharedPreferences
) {

    val db=Firebase.firestore.collection("messages")

    fun sendMessage(text:String,listener:(Result<Boolean>)->Unit){
        val uid=prefs.getString("id","0") ?: "0"
        val name=prefs.getString("name","User") ?: "User"
        val message=Message(uid,name,text)

        db.add(message).addOnCompleteListener {
            listener(Result(it.isSuccessful,it.isSuccessful,it.exception?.message))
        }
    }

    fun getMessages(listener: (Result<List<Message>>) -> Unit){
        db.orderBy("timestamp",Query.Direction.DESCENDING).limit(50).addSnapshotListener { value, error ->

            if (error!=null) {
                listener(Result(false, null, error.message))
                return@addSnapshotListener
            }

            if(value!=null){
                val messages=value.toObjects(Message::class.java)
                listener(Result(true,messages))
            }

        }
    }

}