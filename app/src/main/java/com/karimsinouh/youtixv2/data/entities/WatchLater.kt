package com.karimsinouh.youtixv2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchLater(
        @PrimaryKey(autoGenerate = true) val id:Int,
        val videoId:String,
)
