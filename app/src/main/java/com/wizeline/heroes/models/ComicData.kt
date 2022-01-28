package com.wizeline.heroes.models

import com.wizeline.heroes.Thumbnail


data class ComicData(
    val code: Int,
    val status: String,
    val copyright: String,
    val etag: String,
    val data: ComicDataContainer
)

data class ComicDataContainer(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Comic>
)

data class Comic(
    val id: Int,
    val title: String,
    val issueNumber: Double,
    val description: String,
    val thumbnail: Thumbnail

)
