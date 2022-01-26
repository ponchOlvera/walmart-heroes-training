package com.wizeline.heroes

import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.models.ComicData
import com.wizeline.heroes.models.SeriesData
import io.reactivex.Single
import javax.inject.Inject

class RetrofitRepository @Inject constructor(): IRepository {

    override fun getCharacters(
        timestamp: String,
        apikey: String,
        hash: String,
        offset: Int
    ): Single<Characters> {
        return NetworkClient.getServices().characters(timestamp, apikey, hash, offset)
    }

    override fun getComicsByCharacter(
        timestamp: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Single<ComicData> {
        return NetworkClient.getServices().comicsByCharacter(timestamp, apikey, hash, characterId)
    }

    override fun getSeriesByCharacter(
        timestamp: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Single<SeriesData> {
        return NetworkClient.getServices().seriesByCharacter(timestamp, apikey, hash, characterId)
    }
}