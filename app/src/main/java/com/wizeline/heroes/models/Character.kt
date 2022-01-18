package com.wizeline.heroes.models

import com.google.gson.annotations.SerializedName
import com.wizeline.heroes.Thumbnail

data class Character(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("modified") val modified: String,
    @SerializedName("comics") val comics: ComicList,
    @SerializedName("thumbnail") val thumbnail: Thumbnail,
    @SerializedName("resourceURI") val resourceURI: String
)

data class ComicList(
    @SerializedName("available") val available: Int,
    @SerializedName("returned") val returned: Int,
    @SerializedName("collectionURI") val collectionUri: String,
    @SerializedName("items") val comicList: List<Comic>
)

data class Comic(
    @SerializedName("resourceURI") val path: String,
    @SerializedName("name") val name: String
)
