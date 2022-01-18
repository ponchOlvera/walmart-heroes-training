package com.wizeline.heroes

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {

    private var retrofitInstance: Retrofit? = null

    init {
        if (retrofitInstance == null) createRetrofitInstance()
    }

    fun getServices(): HeroesServices {
        return retrofitInstance!!.create(HeroesServices::class.java)
    }

    private fun createRetrofitInstance(){
        retrofitInstance = Retrofit.Builder().apply {
            baseUrl("https://gateway.marvel.com/v1/public/")
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }.build()
    }
}
