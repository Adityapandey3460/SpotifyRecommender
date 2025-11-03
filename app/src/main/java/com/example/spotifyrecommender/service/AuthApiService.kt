package com.example.spotifyrecommender.service

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {
    @FormUrlEncoded
    @POST("api/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): TokenResponse
}

data class TokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)