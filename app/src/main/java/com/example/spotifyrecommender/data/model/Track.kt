package com.example.spotifyrecommender.data.model

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("artists") val artists: List<Artist>,
    @SerializedName("album") val album: Album,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("duration_ms") val durationMs: Int,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("uri") val uri: String,
    @SerializedName("external_urls") val externalUrls: ExternalUrls
)

data class Artist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String
)

data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("images") val images: List<Image>,
    @SerializedName("album_type") val albumType: String,
    @SerializedName("uri") val uri: String
)

data class Image(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)

data class ExternalUrls(
    @SerializedName("spotify") val spotify: String
)

data class RecommendationResponse(
    @SerializedName("tracks") val tracks: List<Track>
)