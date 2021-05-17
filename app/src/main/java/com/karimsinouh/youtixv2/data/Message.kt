package com.karimsinouh.youtixv2.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    val uid:String?="0",
    val userName:String?="User",
    val message:String?="",
    @ServerTimestamp val timestamp:Date?=null,
    @DocumentId val id:String?=null,
)
