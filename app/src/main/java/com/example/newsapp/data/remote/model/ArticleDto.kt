package com.example.newsapp.data.remote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "articles")
data class ArticleDto(
    @PrimaryKey val id: Int,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val content: String?
) : Parcelable {

}