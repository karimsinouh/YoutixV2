package com.karimsinouh.youtixv2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* param current millis: the watched time
* */
@Entity
data class HistoryItem(
        val videoId:String,
        val duration:Int,
        val currentMillis:Int,
        @PrimaryKey(autoGenerate = true) val id:Int?=0,
)
