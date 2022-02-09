package com.wizeline.heroes

import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.models.ComicData
import com.wizeline.heroes.models.SeriesData
import com.wizeline.heroes.ui.abstractClasses.BaseApiResponse
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class RetrofitRepository @Inject constructor() : IRepository, BaseApiResponse() {

    override suspend fun getCharacters(
        timestamp: String,
        apikey: String,
        hash: String,
        offset: Int,
        startsWith: String?
    ): Response<Characters> {
        return NetworkClient.getServices().characters(timestamp, apikey, hash, offset, startsWith)
    }

    override suspend fun getCharactersAsFlow(
        timestamp: String,
        apikey: String,
        hash: String,
        offset: Int,
        startsWith: String?
    ): GetMarvelCharactersUseCase.NetworkResult<Characters> {

        return safeApiCall {
            NetworkClient.getServices().characters(timestamp, apikey, hash, offset, startsWith)
        }
    }

    override fun getComicsByCharacter(
        timestamp: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Single<ComicData> {
        return NetworkClient.getServices().comicsByCharacter(characterId, timestamp, apikey, hash)
    }

    override fun getSeriesByCharacter(
        timestamp: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Single<SeriesData> {
        return NetworkClient.getServices().seriesByCharacter(characterId, timestamp, apikey, hash)
    }
}