package com.wizeline.heroes.interfaces

import com.wizeline.heroes.Characters
import com.wizeline.heroes.models.ComicData
import com.wizeline.heroes.models.SeriesData
import io.reactivex.Single
import retrofit2.Response

interface IRepository {

    suspend fun getCharacters(
        timestamp: String,
        apikey: String,
        hash: String,
        offset: Int,
        startsWith: String?,
    ): Response<Characters>

    fun getComicsByCharacter(
        timestamp: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Single<ComicData>

    fun getSeriesByCharacter(
        timestamp: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Single<SeriesData>
}