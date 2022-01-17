package com.wizeline.heroes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient {

    fun getServices(): HeroesServices {
        return Retrofit.Builder().apply {
            baseUrl("https://gateway.marvel.com/v1/public/")
            addConverterFactory(GsonConverterFactory.create())
        }.build().create(HeroesServices::class.java)
    }
}
