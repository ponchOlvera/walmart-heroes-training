package com.wizeline.heroes

import com.wizeline.heroes.models.ComicData
import com.wizeline.heroes.models.SeriesData
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HeroesServices {

    @GET("characters")
    fun characters(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
    ): Single<Characters>

    @GET("characters/{characterId}/comics")
    fun comicsByCharacter(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Path("characterId") characterId: Int,
    ): Single<ComicData>

    @GET("characters/{characterId}/series")
    fun seriesByCharacter(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Path("characterId") characterId: Int,
    ): Single<SeriesData>
}
