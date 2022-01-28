package com.wizeline.heroes.models

import android.content.Context
import com.wizeline.heroes.Thumbnail

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val modified: String,
    val comics: ComicList,
    val thumbnail: Thumbnail,
    val resourceURI: String
): java.io.Serializable{
}

data class ComicList(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<ComicSummary>
): java.io.Serializable

data class ComicSummary(
    val name: String,
    val resourceURI: String,
): java.io.Serializable
