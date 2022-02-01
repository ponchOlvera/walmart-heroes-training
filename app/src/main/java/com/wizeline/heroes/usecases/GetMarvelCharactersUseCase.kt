package com.wizeline.heroes.usecases

import com.wizeline.heroes.Endpoint.API_KEY
import com.wizeline.heroes.Endpoint.PRIVATE_KEY
import com.wizeline.heroes.di.Retrofit
import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.models.Character
import com.wizeline.heroes.toMD5
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetMarvelCharactersUseCase @Inject constructor(
    @Retrofit var repository: IRepository,
) {

    /** WIZELINE KEYS **/
    /*private val privateKey = "244be7c2496cbf6d331145cc489b4892457cc2c0"
    private val apikey = "6ffcf49b680b7250a6983acd33731f55"*/

    private val timestamp = System.currentTimeMillis().toString()

    // TODO: 24/01/22 Delete livedata instance and return single response to ViewModel.
    operator fun invoke(offset: Int, startsWith: String?): Single<List<Character>> {
        val hash = (timestamp + PRIVATE_KEY + API_KEY).toMD5()
        // Use disposable to prevent observable memory leaks
        return repository.getCharacters(timestamp, API_KEY, hash, offset, startsWith)
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribeOn(Schedulers.io())
            .map {
                it.data.results
            }
    }
}