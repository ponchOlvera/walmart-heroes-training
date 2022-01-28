package com.wizeline.heroes

import com.wizeline.heroes.Endpoint.CHARACTERS_URL
import com.wizeline.heroes.Endpoint.COMICS_URL
import com.wizeline.heroes.Endpoint.SERIES_URL
import com.wizeline.heroes.models.ComicData
import com.wizeline.heroes.models.SeriesData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HeroesServices {

    @GET(CHARACTERS_URL)
    fun characters(
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
    ): Single<Characters>

    @GET(COMICS_URL)
    fun comicsByCharacter(
        @Path("characterId") characterId: Int,
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
    ): Single<ComicData>

    @GET(SERIES_URL)
    fun seriesByCharacter(
        @Path("characterId") characterId: Int,
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
    ): Single<SeriesData>
}
