package com.wizeline.heroes

import com.wizeline.heroes.Endpoint.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {

    private var retrofitInstance: Retrofit? = null

    init {
        createRetrofitInstanceIfNull()
    }

    fun getServices(): HeroesServices {
            return createRetrofitInstanceIfNull().create(HeroesServices::class.java)
    }

    private fun createRetrofitInstanceIfNull(): Retrofit{
        if (retrofitInstance == null){
            retrofitInstance = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            }.build()
        }
        return retrofitInstance!!
    }
}

object Endpoint {
    const val BASE_URL = "https://gateway.marvel.com/v1/public/"
    const val COMICS_URL = "characters/{characterId}/comics"
    const val CHARACTERS_URL = "characters"
    const val SERIES_URL = "characters/{characterId}/series"
    // etc.
}
