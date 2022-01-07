package com.example.newsapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.model.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE articleUrl = :url")
    suspend fun remoteKeysArticleUrl(url: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()

}