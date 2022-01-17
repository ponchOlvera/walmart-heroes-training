package com.wizeline.heroes

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface HeroesServices {

    @GET("characters")
    fun characters(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int,
    ): Observable<Characters>
}
