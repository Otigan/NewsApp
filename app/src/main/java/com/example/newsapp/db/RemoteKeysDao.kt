package com.example.newsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE url =:url")
    suspend fun remoteKeysUrl(url: String): RemoteKeys?

    @Query("DELETE FROM remote_keys WHERE isBreaking = 1")
    suspend fun deleteBreakingRemoteKeys()

    @Query("DELETE FROM remote_keys WHERE isBreaking = 0")
    suspend fun clearRemoteKeys()

}