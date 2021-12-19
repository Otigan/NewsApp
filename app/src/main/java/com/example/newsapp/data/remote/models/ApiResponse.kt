package com.example.newsapp.data.remote.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
) : Parcelable