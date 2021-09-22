package com.example.newsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class NewsRemoteKeys(@PrimaryKey val id: String, val prev: Int?, val next: Int?)