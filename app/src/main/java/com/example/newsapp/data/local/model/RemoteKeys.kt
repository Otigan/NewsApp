package com.example.newsapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val articleId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)