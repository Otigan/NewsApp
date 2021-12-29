package com.example.newsapp.data.remote

import com.example.newsapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest =
            chain.request().newBuilder().addHeader("Authorization", BuildConfig.API_KEY).build()
        return chain.proceed(newRequest)
    }
}