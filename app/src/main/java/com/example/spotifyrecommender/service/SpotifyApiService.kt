package com.example.spotifyrecommender.service

import com.example.spotifyrecommender.data.model.RecommendationResponse
import com.example.spotifyrecommender.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApiService {

    // Search endpoint
    @GET("search")
    suspend fun searchTracks(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 6,  // Reduced for search
        @Query("market") market: String = "US"
    ): SearchResponse

    // Recommendations based on specific track
    @GET("recommendations")
    suspend fun getRecommendationsBasedOnTrack(
        @Header("Authorization") token: String,
        @Query("seed_tracks") trackId: String,
        @Query("limit") limit: Int = 20,
        @Query("market") market: String = "US"
    ): RecommendationResponse

    // General recommendations fallback
    @GET("recommendations")
    suspend fun getGeneralRecommendations(
        @Header("Authorization") token: String,
        @Query("seed_genres") genre: String = "pop",
        @Query("limit") limit: Int = 20,
        @Query("market") market: String = "US"
    ): RecommendationResponse
}