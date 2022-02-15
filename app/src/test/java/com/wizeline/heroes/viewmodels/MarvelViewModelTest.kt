package com.wizeline.heroes.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.wizeline.heroes.Endpoint
import com.wizeline.heroes.RetrofitRepository
import com.wizeline.heroes.StringUtils.Companion.STRING_EMPTY
import com.wizeline.heroes.TestUtils.Companion.RETROFIT_ERROR_RESPONSE
import com.wizeline.heroes.TestUtils.Companion.RETROFIT_SUCCESS_RESPONSE
import com.wizeline.heroes.TestUtils.Companion.SEARCH_QUERY
import com.wizeline.heroes.TestUtils.Companion.getOrAwaitValue
import com.wizeline.heroes.di.CoroutineDispatcherProvider
import com.wizeline.heroes.rules.CoroutineTestRule
import com.wizeline.heroes.toMD5
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MarvelViewModelTest {

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutine = CoroutineTestRule()

    @FlowPreview
    private lateinit var marvelViewModel: MarvelViewModel
    private lateinit var useCase: GetMarvelCharactersUseCase

    private val timestamp = System.currentTimeMillis().toString()
    private val hash = (timestamp + Endpoint.PRIVATE_KEY + Endpoint.API_KEY).toMD5()
    private val DEFAULT_OFFSET = 5

    @FlowPreview
    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        val retrofitResponse = RETROFIT_SUCCESS_RESPONSE
        val retrofitErrorResponse = RETROFIT_ERROR_RESPONSE
        val repository = mockkClass(RetrofitRepository::class)

        //Success Response
        coEvery {
            repository.getCharacters(
                timestamp,
                Endpoint.API_KEY,
                hash,
                DEFAULT_OFFSET,
                null
            )
        } coAnswers { retrofitResponse }

        //Success Response with search param
        coEvery {
            repository.getCharacters(
                timestamp,
                Endpoint.API_KEY,
                hash,
                DEFAULT_OFFSET,
                SEARCH_QUERY
            )
        } coAnswers { retrofitResponse }

        // Error Response
        coEvery {
            repository.getCharacters(
                STRING_EMPTY,
                Endpoint.API_KEY,
                hash,
                DEFAULT_OFFSET,
                null
            )
        } coAnswers { retrofitErrorResponse }

        useCase = GetMarvelCharactersUseCase(repository)
        marvelViewModel =
            MarvelViewModel(useCase, CoroutineDispatcherProvider(testCoroutine.dispatcher))
    }

    @FlowPreview
    @Test
    fun `call to getCharacters emits non null viewState`() {
        marvelViewModel.getCharacters(DEFAULT_OFFSET, timestamp, hash)

        val viewState = marvelViewModel.marvelViewState.getOrAwaitValue()

        assertThat(viewState).isNotNull()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `call to getCharacters emits non empty list in the viewState`() = runTest {
        marvelViewModel.getCharacters(DEFAULT_OFFSET, timestamp, hash)
        val viewState = marvelViewModel.marvelViewState.getOrAwaitValue()
        assertThat(viewState.characterList).isNotEmpty()
    }

    @Test
    fun `call to getCharacters emits a valid MarvelViewState for the UI`() {
        marvelViewModel.getCharacters(DEFAULT_OFFSET, timestamp, hash)
        val viewState = marvelViewModel.marvelViewState.getOrAwaitValue()
        assertThat(viewState.characterList).isNotEmpty()
        assertThat(viewState.characterList.size).isEqualTo(5)
        assertThat(viewState.isListUpdated).isTrue()
        assertThat(viewState.clearRecyclerData).isFalse()
    }

    @Test
    fun `MarvelViewState is render correctly when value are passed through the viewModel`() {
        marvelViewModel.setLoading(true)
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().isLoading).isTrue()
        marvelViewModel.togglePagination(true)
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().enablePagination).isTrue()
    }

    @Test
    fun `after getCharacters is successful, a the viewSate indicates that the list is updated, then the list updated flag is set to false and no character list is returned`() {
        marvelViewModel.getCharacters(DEFAULT_OFFSET, timestamp, hash)
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().characterList.size).isEqualTo(
            DEFAULT_OFFSET
        )
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().isListUpdated).isTrue()
        marvelViewModel.listUpdated()
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().characterList).isEmpty()
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().isListUpdated).isFalse()
    }

    @Test
    fun `when a search query is included, the viewState requires a recyclerData clear`() {
        marvelViewModel.includeSearchQuery(SEARCH_QUERY)
        marvelViewModel.getCharacters(DEFAULT_OFFSET, timestamp, hash)
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().characterList).isNotEmpty()
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().clearRecyclerData).isTrue()
    }

    @Test
    fun `Error request`(){
        marvelViewModel.includeSearchQuery("")
        marvelViewModel.getCharacters(DEFAULT_OFFSET, STRING_EMPTY, hash)
        println(marvelViewModel.marvelViewState.getOrAwaitValue().error)
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().error).isNotEmpty()
        assertThat(marvelViewModel.marvelViewState.getOrAwaitValue().characterList).isEmpty()
    }

}