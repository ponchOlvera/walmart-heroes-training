package com.wizeline.heroes

import com.wizeline.heroes.interfaces.IRepository
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
}