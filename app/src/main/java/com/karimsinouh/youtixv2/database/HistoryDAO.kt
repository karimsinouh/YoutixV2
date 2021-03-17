package com.karimsinouh.youtixv2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.karimsinouh.youtixv2.data.entities.HistoryItem


@Dao
interface HistoryDAO {

    @Query("SELECT * FROM HistoryItem")
    fun list():LiveData<List<HistoryItem>>

    @Query("SELECT * FROM HistoryItem WHERE videoId =:videoId")
    suspend fun get(videoId: String):HistoryItem

    @Insert(entity = HistoryItem::class)
    suspend fun add(item:HistoryItem)

    @Query("DELETE FROM HistoryItem WHERE videoId=:videoId")
    suspend fun delete(videoId: String)

    @Query(" SELECT EXISTS (SELECT * FROM HistoryItem WHERE videoId=:videoId) ")
    suspend fun exists(videoId:String):Boolean

    @Query("UPDATE HistoryItem SET currentMillis =:millis WHERE videoId=:videoId ")
    suspend fun updateMillis(videoId:String,millis:Int)

    @Query("DELETE  FROM HistoryItem")
    fun clear()

}