package com.karimsinouh.youtixv2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.karimsinouh.youtixv2.data.entities.SearchHistory
import com.karimsinouh.youtixv2.data.items.SearchItem

@Dao
interface SearchHistoryDAO {

    @Query("SELECT * FROM SearchHistory ORDER BY ID DESC LIMIT 5 ")
    suspend fun list():List<SearchHistory>

    @Query("SELECT * FROM SearchHistory WHERE `query` LIKE :query ORDER BY ID DESC LIMIT 5")
    suspend fun search(query:String):List<SearchHistory>

    @Insert(entity = SearchHistory::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(item:SearchHistory)

    @Delete(entity = SearchHistory::class)
    suspend fun delete(item:SearchHistory)

    @Query("DELETE  FROM SearchHistory")
    fun clear()

    @Query("SELECT EXISTS (SELECT 1 FROM SearchHistory WHERE `query` = :q) ")
    suspend fun exists(q: String): Boolean

}