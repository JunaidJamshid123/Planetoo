package com.example.planetapp.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit singleton client for API calls
 */
object RetrofitClient {
    
    // API Key for Solar System OpenData API
    // Get your own key at: https://api.le-systeme-solaire.net/generatekey.html
    private const val API_KEY = "f894d4d0-9c27-458e-9fd6-60e3409ea19f"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "SolarScope/1.0 Android")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer $API_KEY")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(SolarSystemApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val solarSystemApi: SolarSystemApi = retrofit.create(SolarSystemApi::class.java)
}
