package com.karimsinouh.youtixv2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.karimsinouh.youtixv2.data.entities.SearchHistory

@Dao
interface SearchHistoryDAO {

    @Query("SELECT * FROM SearchHistory LIMIT 5")
    fun list():LiveData<List<SearchHistory>>

    @Insert(entity = SearchHistory::class)
    fun add(item:SearchHistory)

    @Delete(entity = SearchHistory::class)
    fun delete(item:SearchHistory)

    @Query("DELETE  FROM SearchHistory")
    fun clear()

}