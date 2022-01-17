package com.wizeline.heroes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HeroesServices {

    @GET("characters")
    fun characters(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
    ): Call<Characters>
}
