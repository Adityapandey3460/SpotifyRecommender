package com.example.spotifyrecommender.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("tracks") val tracks: TracksResponse
)

data class TracksResponse(
    @SerializedName("items") val items: List<Track>,
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("href") val href: String,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?
)