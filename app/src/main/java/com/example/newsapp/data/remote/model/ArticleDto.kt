package com.example.newsapp.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleDto(

    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val content: String?
) : Parcelable {

}