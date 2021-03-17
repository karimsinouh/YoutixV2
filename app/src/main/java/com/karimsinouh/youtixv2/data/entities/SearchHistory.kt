package com.karimsinouh.youtixv2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
        val query:String,
        @PrimaryKey(autoGenerate = true) val id:Int?=null,
)