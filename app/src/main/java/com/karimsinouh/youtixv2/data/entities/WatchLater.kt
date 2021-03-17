package com.karimsinouh.youtixv2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchLater(
        @PrimaryKey val videoId:String
)
