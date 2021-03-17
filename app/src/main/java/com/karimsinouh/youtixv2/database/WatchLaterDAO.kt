package com.karimsinouh.youtixv2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karimsinouh.youtixv2.data.entities.WatchLater


@Dao
interface WatchLaterDAO {

    @Query("SELECT * FROM WatchLater")
    fun list():LiveData<List<WatchLater>>

    @Query("DELETE FROM WatchLater WHERE videoId =:videoId")
    suspend fun delete(videoId:String)

    @Insert(entity = WatchLater::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(item:WatchLater)

    @Query("SELECT EXISTS (SELECT 1 FROM WatchLater WHERE videoId=:videoId) ")
    suspend fun exists(videoId:String):Boolean

    @Query("DELETE  FROM WatchLater")
    fun clear()

}