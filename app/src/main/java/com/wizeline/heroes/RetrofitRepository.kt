package com.wizeline.heroes

import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.models.ComicData
import com.wizeline.heroes.models.SeriesData
import io.reactivex.Observable

class RetrofitRepository: IRepository {

    override fun getCharacters(
        ts: String,
        apikey: String,
        hash: String,
        offset: Int
    ): Observable<Characters> {
        return NetworkClient.getServices().characters(ts, apikey, hash, offset)
    }

    override fun getComicsByCharacter(
        ts: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Observable<ComicData> {
        return NetworkClient.getServices().comicsByCharacter(ts, apikey, hash, characterId)
    }

    override fun getSeriesByCharacter(
        ts: String,
        apikey: String,
        hash: String,
        characterId: Int
    ): Observable<SeriesData> {
        return NetworkClient.getServices().seriesByCharacter(ts, apikey, hash, characterId)
    }
}