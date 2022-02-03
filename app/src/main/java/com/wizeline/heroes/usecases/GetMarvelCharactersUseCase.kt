package com.wizeline.heroes.usecases

import com.wizeline.heroes.Characters
import com.wizeline.heroes.Endpoint.API_KEY
import com.wizeline.heroes.Endpoint.PRIVATE_KEY
import com.wizeline.heroes.di.Retrofit
import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.toMD5
import retrofit2.Response
import javax.inject.Inject

class GetMarvelCharactersUseCase @Inject constructor(
    @Retrofit var repository: IRepository,
) {

    private val timestamp = System.currentTimeMillis().toString()

    // TODO: 24/01/22 Delete livedata instance and return single response to ViewModel.
    suspend operator fun invoke(offset: Int, startsWith: String?): Response<Characters> {
        val hash = (timestamp + PRIVATE_KEY + API_KEY).toMD5()
        return repository.getCharacters(timestamp, API_KEY, hash, offset, startsWith)
        // Use disposable to prevent observable memory leaks
        /*return repository.getCharacters(timestamp, API_KEY, hash, offset, startsWith)
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribeOn(Schedulers.io())
            .map {
                it.data.results
            }*/
    }
}