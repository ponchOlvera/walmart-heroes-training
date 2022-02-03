package com.wizeline.heroes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.util.HalfSerializer.onError
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MarvelViewModel @Inject constructor(val getMarvelCharactersUseCase: GetMarvelCharactersUseCase) :
    ViewModel() {

    private var _marvelViewState = MutableLiveData(MarvelViewState(arrayListOf(), "", false))
    private var searchQuery: String? = null
    private var clearRecyclerData: Boolean = false
    private var job: Job? = null
    val marvelViewState get() = _marvelViewState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        requestError(throwable.message)
    }

    private fun requestError(message: String?) {
        _marvelViewState.value = MarvelViewState(
            error = message
        )
    }

    fun getCharacters(offset: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = getMarvelCharactersUseCase(offset, searchQuery)
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