package com.codebyashish.chatai.networking

import com.codebyashish.chatai.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
     val KEY_CONTENT_TYPE = "Content-Type"
     val APPLICATION_URL_ENCODED = "application/json"
     val KEY_AUTHORIZATION = "Authorization"

    fun interceptor(apiKey : String) : Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val accessToken = "Bearer $apiKey"

            val request2 = original.newBuilder()
                .header(KEY_CONTENT_TYPE, APPLICATION_URL_ENCODED)
                .header(KEY_AUTHORIZATION, accessToken)
                .method(original.method, original.body)
                .build()
            chain.proceed(request2)
        }
    }
}