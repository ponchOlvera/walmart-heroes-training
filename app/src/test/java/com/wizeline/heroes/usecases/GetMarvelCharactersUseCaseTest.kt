package com.wizeline.heroes.usecases

import com.google.common.truth.Truth.assertThat
import com.wizeline.heroes.Endpoint.API_KEY
import com.wizeline.heroes.Endpoint.PRIVATE_KEY
import com.wizeline.heroes.RetrofitRepository
import com.wizeline.heroes.StringUtils.Companion.STRING_EMPTY
import com.wizeline.heroes.TestUtils.Companion.RETROFIT_ERROR_RESPONSE
import com.wizeline.heroes.TestUtils.Companion.RETROFIT_SUCCESS_RESPONSE
import com.wizeline.heroes.toMD5
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test


class GetMarvelCharactersUseCaseTest {

    @MockK
    private lateinit var useCase: GetMarvelCharactersUseCase
    private val timestamp = System.currentTimeMillis().toString()
    private val hash = (timestamp + PRIVATE_KEY + API_KEY).toMD5()
    private val DEFAULT_OFFSET = 20
    private lateinit var repository: RetrofitRepository


    @Before
    fun setUp() {

        val retrofitSuccessResponse = RETROFIT_SUCCESS_RESPONSE
        val retrofitErrorResponse = RETROFIT_ERROR_RESPONSE
        //val retrofitErrorResponse = Response.error<>()
        repository = mockkClass(RetrofitRepository::class)

        // Success Response
        coEvery {
            repository.getCharacters(
                timestamp,
                API_KEY,
                hash,
                DEFAULT_OFFSET,
                null
            )
        } coAnswers { retrofitSuccessResponse }

        // Error Response
        coEvery {
            repository.getCharacters(
                STRING_EMPTY,
                API_KEY,
                hash,
                DEFAULT_OFFSET,
                null
            )
        } coAnswers { retrofitErrorResponse }

        useCase = GetMarvelCharactersUseCase(repository)
    }

    @Test
    fun `success response when use case is invoke`() = runBlocking {
        val response = useCase(DEFAULT_OFFSET, null, timestamp, hash)
        withContext(Dispatchers.Default) {
            assertThat(
                response.isSuccessful
            ).isNotNull()
            assertThat(response.isSuccessful).isTrue()
        }
        assertThat(response.isSuccessful).isTrue()
    }

    @Test
    fun `success response and characters list response not empty when use case is invoke`() = runBlocking {
        val response = useCase(DEFAULT_OFFSET, null, timestamp, hash)
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()?.data?.results).isNotEmpty()
    }

    @Test
    fun `error response is returned from use case, therefore, no body is returned`() = runBlocking {
        val response = useCase(DEFAULT_OFFSET, null, STRING_EMPTY, hash)
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.body()).isNull()
    }
}