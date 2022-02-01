package com.wizeline.heroes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MarvelViewModel @Inject constructor(val getMarvelCharactersUseCase: GetMarvelCharactersUseCase) :
    ViewModel() {

    private var _marvelViewState = MutableLiveData(MarvelViewState(arrayListOf(), "", false))
    private var searchQuery: String? = null
    private var clearRecyclerData: Boolean = false
    val marvelViewState get() = _marvelViewState

    private val compositeDisposable = CompositeDisposable()

    fun getCharacters(offset: Int) {
        compositeDisposable.add(getMarvelCharactersUseCase(offset, searchQuery).subscribe({
            val marvelCharacterList = it
            _marvelViewState.value = MarvelViewState(
                characterList = marvelCharacterList,
                clearRecyclerData = clearRecyclerData,
                isListUpdated = true
            )
            if (clearRecyclerData) {
                clearRecyclerData = false
            }
        }, {
            _marvelViewState.value = MarvelViewState(
                error = it.message
            )
            it.printStackTrace()
        }))
    }

    override fun onCleared() {
        compositeDisposable.clear()
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