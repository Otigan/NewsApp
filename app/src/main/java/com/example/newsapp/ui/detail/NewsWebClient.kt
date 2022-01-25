package com.example.newsapp.ui.detail

import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi

class NewsWebClient(private val articleUrl: String) : WebViewClient() {

    // Deprecated in api 24, added for compatibility
    @SuppressWarnings("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val regex = Regex("www\\.|m\\.")
        val newUrl = url?.replace(regex, "")
        val newArticleUrl = articleUrl.replace(regex, "")
        //Log.d("WebView", "newUrl: $newUrl")
        //Log.d("WebView", "newArticleUrl: $articleUrl")

        if (newUrl == newArticleUrl) {
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val regex = Regex("www\\.|m\\.")
        val url = request?.url.toString()

        val newUrl = url.replace(regex, "")
        val newArticleUrl = articleUrl.replace(regex, "")
        //Log.d("WebView", "newUrl: $newUrl")
        //Log.d("WebView", "newArticleUrl: $articleUrl")

        if (newUrl == newArticleUrl) {
            return false
        }
        return true
    }

}