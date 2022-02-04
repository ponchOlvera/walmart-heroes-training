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

    // TODO: 24/01/22 Delete livedata instance and return single response to ViewModel.
    suspend operator fun invoke(offset: Int, startsWith: String?, timestamp: String, hash: String): Response<Characters> {
        return repository.getCharacters(timestamp, API_KEY, hash, offset, startsWith)
    }
}