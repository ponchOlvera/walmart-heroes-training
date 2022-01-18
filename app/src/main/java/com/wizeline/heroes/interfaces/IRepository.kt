package com.wizeline.heroes.interfaces

import com.wizeline.heroes.Characters
import com.wizeline.heroes.models.ComicData
import com.wizeline.heroes.models.SeriesData
import io.reactivex.Observable

interface IRepository {

    fun getCharacters(
        ts: String,
        apikey: String,
        hash: String,
        offset: Int
    ): Observable<Characters>

    fun getComicsByCharacter(
        ts: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Observable<ComicData>

    fun getSeriesByCharacter(
        ts: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Observable<SeriesData>
}