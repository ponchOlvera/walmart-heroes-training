package com.wizeline.heroes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizeline.heroes.di.CoroutineDispatcherProvider
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MarvelViewModel @Inject constructor(
    val getMarvelCharactersUseCase: GetMarvelCharactersUseCase,
    val dispatcher: CoroutineDispatcherProvider
) :
    ViewModel() {

    private var _marvelViewState = MutableLiveData(MarvelViewState(arrayListOf(), "", false))
    private var searchQuery: String? = null
    private var clearRecyclerData: Boolean = false
    private var job: Job? = null
    val marvelViewState: LiveData<MarvelViewState> get() = _marvelViewState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        requestError(throwable.message)
    }

    private fun requestError(message: String?) {
        _marvelViewState.value = MarvelViewState(
            error = message
        )
    }

    fun getCharacters(offset: Int, timestamp: String, hash: String) {
        job = CoroutineScope(dispatcher.name + exceptionHandler).launch {
            val response = getMarvelCharactersUseCase(offset, searchQuery, timestamp, hash)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val marvelCharactersList = response.body()?.data?.results ?: listOf()
                    _marvelViewState.value = MarvelViewState(
                        characterList = marvelCharactersList,
                        clearRecyclerData = clearRecyclerData,
                        isListUpdated = true
                    )
                } else {
                    requestError(response.message())
                }
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

    fun setLoading(loading: Boolean) {
        _marvelViewState.value = MarvelViewState(
            isLoading = loading
        )
    }

    fun listUpdated() {
        _marvelViewState.value = MarvelViewState(
            isListUpdated = false
        )
    }

    fun includeSearchQuery(query: String) {
        searchQuery = if (query.isNotEmpty()) query else null
        clearRecyclerData = true
    }

    fun togglePagination(enablePagination: Boolean) {
        _marvelViewState.value = MarvelViewState(
            enablePagination = enablePagination
        )
    }

}