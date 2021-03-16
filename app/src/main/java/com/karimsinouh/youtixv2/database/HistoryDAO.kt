package com.karimsinouh.youtixv2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.karimsinouh.youtixv2.data.entities.HistoryItem


@Dao
interface HistoryDAO {

    @Query("SELECT 1 FROM HistoryItem")
    fun list():LiveData<List<HistoryItem>>

    @Query("SELECT * FROM HistoryItem WHERE id =:videoId")
    suspend fun get(videoId: String):HistoryItem

    @Insert(entity = HistoryItem::class)
    suspend fun add(item:HistoryItem)

    @Delete(entity = HistoryItem::class)
    suspend fun delete(item:HistoryItem)

    @Query(" SELECT EXISTS (SELECT * FROM HistoryItem WHERE videoId=:videoId) ")
    suspend fun exists(videoId:String):Boolean

    @Query("UPDATE HistoryItem SET currentMillis =:millis WHERE videoId=:videoId ")
    suspend fun updateMillis(videoId:String,millis:Long)

    @Query("DELETE  FROM HistoryItem")
    fun clear()

}