package com.example.spotifyrecommender.data.repository

import com.example.spotifyrecommender.data.model.Track
import com.example.spotifyrecommender.service.SpotifyApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class SpotifyRepository {
    private val api: SpotifyApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyApiService::class.java)
    }

    suspend fun searchTracks(token: String, query: String): List<Track> {
        return try {
            println("🔍 Searching for: $query")
            val response = api.searchTracks("Bearer $token", query)
            println("✅ Search found ${response.tracks.items.size} tracks")
            response.tracks.items
        } catch (e: Exception) {
            println("❌ Search error: ${e.message}")
            emptyList()
        }
    }

    suspend fun getPopularTracks(token: String): List<Track> {
        return try {
            println("🎵 Getting popular tracks...")
            val response = api.searchTracks("Bearer $token", "popular")
            println("✅ Got ${response.tracks.items.size} popular tracks")
            response.tracks.items
        } catch (e: Exception) {
            println("❌ Popular tracks error: ${e.message}")
            emptyList()
        }
    }

    // NEW: Get recommendations based on a specific track
    suspend fun getRecommendationsBasedOnTrack(token: String, trackId: String): List<Track> {
        return try {
            println("🎵 Getting recommendations based on track: $trackId")
            val response = api.getRecommendationsBasedOnTrack("Bearer $token", trackId)
            println("✅ Got ${response.tracks.size} recommendations")
            response.tracks
        } catch (e: Exception) {
            println("❌ Track-based recommendations failed: ${e.message}")
            emptyList()
        }
    }

    // Fallback: General recommendations
    suspend fun getGeneralRecommendations(token: String): List<Track> {
        return try {
            println("🎵 Getting general recommendations...")
            val response = api.getGeneralRecommendations("Bearer $token")
            println("✅ Got ${response.tracks.size} general recommendations")
            response.tracks
        } catch (e: Exception) {
            println("❌ General recommendations failed: ${e.message}")
            emptyList()
        }
    }
}