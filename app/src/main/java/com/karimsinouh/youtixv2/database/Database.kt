package com.karimsinouh.youtixv2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.karimsinouh.youtixv2.data.entities.HistoryItem
import com.karimsinouh.youtixv2.data.entities.SearchHistory
import com.karimsinouh.youtixv2.data.entities.WatchLater

@Database( version = 3, entities = [HistoryItem::class,WatchLater::class,SearchHistory::class])
abstract class Database: RoomDatabase() {

    abstract fun history():HistoryDAO
    abstract fun watchLater():WatchLaterDAO
    abstract fun searchHistory():SearchHistoryDAO



}