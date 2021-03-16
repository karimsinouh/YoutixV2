package com.karimsinouh.youtixv2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* param current millis: the watched time
* */
@Entity
data class HistoryItem(
        @PrimaryKey(autoGenerate = true) val id:Int,
        val videoId:String,
        val duration:Long,
        val currentMillis:Long
)
