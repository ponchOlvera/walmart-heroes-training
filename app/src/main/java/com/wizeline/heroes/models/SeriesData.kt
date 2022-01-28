package com.wizeline.heroes.models

import com.wizeline.heroes.Thumbnail

data class SeriesData(
    val code: Int,
    val status: String,
    val copyright: String,
    val etag: String,
    val data: SeriesDataContainer
)

data class SeriesDataContainer(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Series>)

data class Series(
    val id: Int,
    val title: String,
    val rating: String,
    val description: String,
    val thumbnail: Thumbnail
)
