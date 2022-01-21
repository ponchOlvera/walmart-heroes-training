package com.wizeline.heroes.models

import com.wizeline.heroes.Thumbnail

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val modified: String,
    val comics: ComicList,
    val thumbnail: Thumbnail,
    val resourceURI: String
)

data class ComicList(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val comicList: List<Comic>
)

data class Comic(
    val resourceURI: String,
    val name: String
)
