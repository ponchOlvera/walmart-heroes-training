package com.wizeline.heroes

import com.google.gson.annotations.SerializedName
import com.wizeline.heroes.models.Character

data class Characters(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("copyright") val copyright: String,
    @SerializedName("attributionText") val attributionText: String,
    @SerializedName("attributionHML")val attributionHTML: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("data") val data: Data
)

data class Data(
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<Character>,
)

data class Thumbnail(
    @SerializedName("path") val path: String,
    @SerializedName("extension") val extension: String
)
