package com.wizeline.heroes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizeline.heroes.interfaces.IMarvelFragmentViewModel
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MarvelViewModel @Inject constructor(val getMarvelCharactersUseCase: GetMarvelCharactersUseCase) :
    ViewModel(), IMarvelFragmentViewModel {

    private var _marvelViewState = MutableLiveData(MarvelViewState(arrayListOf(), "", false))
    val marvelViewState get() = _marvelViewState

    private val compositeDisposable = CompositeDisposable()

    fun getCharacters(offset: Int) {
        compositeDisposable.add(getMarvelCharactersUseCase(offset).subscribe({
            val marvelCharacterList = it
            _marvelViewState.value = MarvelViewState(
                characterList = marvelCharacterList,
                isListUpdated = true
            )
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

    override fun setLoading(loading: Boolean) {
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