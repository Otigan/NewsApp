package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val url: String,
    val prevKey: Int?,
    val nextKey: Int?,
    val isBreaking: Boolean?,
    val isLiked: Boolean?
)