package com.karimsinouh.youtixv2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.karimsinouh.youtixv2.data.entities.WatchLater


@Dao
interface WatchLaterDAO {

    @Query("SELECT * FROM WatchLater")
    fun list(item:WatchLater):LiveData<List<WatchLater>>

    @Delete(entity = WatchLater::class)
    suspend fun delete(item:WatchLater)

    @Insert(entity = WatchLater::class)
    suspend fun add(item:WatchLater)

    @Query("SELECT EXISTS (SELECT 1 FROM WatchLater WHERE videoId=:videoId) ")
    suspend fun exists(videoId:String)

    @Query("DELETE  FROM WatchLater")
    fun clear()

}