package com.wizeline.heroes.interfaces

import com.wizeline.heroes.Characters
import io.reactivex.Observable

interface IRepository {

    fun getCharacters(
        ts: String,
        apikey: String,
        hash: String,
        offset: Int
    ): Observable<Characters>
}