package com.wizeline.heroes.usecases

import android.util.Log
import com.wizeline.heroes.Endpoint
import com.wizeline.heroes.Endpoint.API_KEY
import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.models.*
import com.wizeline.heroes.toMD5
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MarvelCharacterDetailsUseCase @Inject constructor(val repository: IRepository) {

    private val timestamp = System.currentTimeMillis().toString()
    val hash = (timestamp + Endpoint.PRIVATE_KEY + API_KEY).toMD5()

    fun getCharacterDetails(characterId: Int): Single<ArrayList<IMarvelDetailsType>>{
        val listDetailsComicsSeries = ArrayList<IMarvelDetailsType>()

        return getCharacterComics(characterId).flatMap {
            it.forEach { comic ->
                listDetailsComicsSeries.add(ComicDetails(comic))
            }
            Single.just(listDetailsComicsSeries)
        }.flatMap {
            getCharacterSeries(characterId).flatMap {
                it.forEach { series ->
                    listDetailsComicsSeries.add(SeriesDetails(series))
                }
                Single.just(listDetailsComicsSeries)
            }
        }
    }

    private fun getCharacterComics(characterId: Int): Single<List<Comic>>{
        return repository.getComicsByCharacter(timestamp, API_KEY, hash, characterId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.data.results
            }
    }

    private fun getCharacterSeries(characterId: Int): Single<List<Series>>{
        return repository.getSeriesByCharacter(timestamp, API_KEY, hash, characterId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.data.results
            }
    }
}