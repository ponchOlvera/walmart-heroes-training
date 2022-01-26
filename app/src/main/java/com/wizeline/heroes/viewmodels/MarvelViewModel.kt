package com.wizeline.heroes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarvelViewModel @Inject constructor(val getMarvelCharactersUseCase: GetMarvelCharactersUseCase) : ViewModel() {

    private var _marvelViewState = MutableLiveData(MarvelViewState(arrayListOf(), "", false))
    val marvelViewState get() = _marvelViewState

    fun getCharacters(offset: Int) {
        getMarvelCharactersUseCase(offset, _marvelViewState)
    }

    override fun onCleared() {
        getMarvelCharactersUseCase.clearCompositeDisposable()
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

}