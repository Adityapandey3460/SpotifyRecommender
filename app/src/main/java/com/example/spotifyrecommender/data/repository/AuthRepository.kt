package com.example.spotifyrecommender.data.repository

import com.example.spotifyrecommender.service.AuthApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import com.example.spotifyrecommender.BuildConfig

class AuthRepository {



    val clientId = BuildConfig.SPOTIFY_CLIENT_ID
    val clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET



    private val authApi: AuthApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/") // ← Should end with slash
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    suspend fun getAccessToken(): String? {
        return withContext(Dispatchers.IO) {
            try {
                println("🔐 Getting Spotify access token...")
                val response = authApi.getAccessToken(
                    clientId = clientId,
                    clientSecret = clientSecret
                )
                println("✅ Token received: ${response.access_token.take(10)}...")
                response.access_token
            } catch (e: Exception) {
                println("❌ Token error: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }
}