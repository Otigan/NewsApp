package com.example.newsapp.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: NewsRemoteKeys)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeysRepoId(id: String): NewsRemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}