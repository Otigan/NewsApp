package com.example.newsapp.data.remote.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "articles")
@Parcelize
data class Article(
    val title: String,
    @PrimaryKey()
    val url: String,
    val urlToImage: String?,
    var isBreakingNews: Boolean?,
    var isLiked: Boolean?
) : Parcelable
