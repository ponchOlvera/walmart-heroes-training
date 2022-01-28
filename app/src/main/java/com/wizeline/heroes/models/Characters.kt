package com.wizeline.heroes

import com.wizeline.heroes.models.Character

data class Characters(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: Data
)

data class Data(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Character>,
)

data class Thumbnail(
    val path: String,
    val extension: String
): java.io.Serializable
