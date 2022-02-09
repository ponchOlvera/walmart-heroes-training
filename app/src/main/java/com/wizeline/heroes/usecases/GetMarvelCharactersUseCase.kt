package com.wizeline.heroes.usecases

import com.wizeline.heroes.Characters
import com.wizeline.heroes.Endpoint.API_KEY
import com.wizeline.heroes.di.Retrofit
import com.wizeline.heroes.interfaces.IRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class GetMarvelCharactersUseCase @Inject constructor(
    @Retrofit var repository: IRepository,
) {

    // TODO: 24/01/22 Delete livedata instance and return single response to ViewModel.
    suspend operator fun invoke(
        offset: Int,
        startsWith: String?,
        timestamp: String,
        hash: String
    ): Response<Characters> {
        return repository.getCharacters(timestamp, API_KEY, hash, offset, startsWith)
    }

    suspend fun asFlow(
        offset: Int,
        startsWith: String?,
        timestamp: String,
        hash: String
    ): Flow<NetworkResult<Characters>> {
        return flow {
            emit(
                repository.getCharactersAsFlow(
                    timestamp,
                    API_KEY,
                    hash,
                    offset,
                    startsWith
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    sealed class NetworkResult<T>(
        val data: T? = null,
        val message: String? = null
    ) {
        class Success<T>(data: T) : NetworkResult<T>(data)
        class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
        class Loading<T> : NetworkResult<T>()
    }

}