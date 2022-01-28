package com.wizeline.heroes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizeline.heroes.interfaces.IMarvelFragmentViewModel
import com.wizeline.heroes.models.ComicDetails
import com.wizeline.heroes.models.MarvelCharacterDetailsViewState
import com.wizeline.heroes.models.SeriesDetails
import com.wizeline.heroes.usecases.MarvelCharacterDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MarvelDetailsViewModel @Inject constructor(val characterDetailsUseCase: MarvelCharacterDetailsUseCase) :
    ViewModel(), IMarvelFragmentViewModel {

    private var _marvelDetailsViewState = MutableLiveData(MarvelCharacterDetailsViewState())
    val marvelDetailsViewState get() = _marvelDetailsViewState

    private val compositeDisposable = CompositeDisposable()

    fun getCharacterDetails(characterId: Int) {
        characterDetailsUseCase.getCharacterDetails(characterId)
            .subscribe({
                val seriesDetails = ArrayList<SeriesDetails>()
                val comicsDetails = ArrayList<ComicDetails>()
                it.forEach { detailType ->
                    when(detailType){
                        is ComicDetails -> comicsDetails.add(detailType)
                        is SeriesDetails -> seriesDetails.add(detailType)
                    }
                }
                _marvelDetailsViewState.value = MarvelCharacterDetailsViewState(
                    comics = comicsDetails,
                    series = seriesDetails
                )
            }, {
                _marvelDetailsViewState.value = MarvelCharacterDetailsViewState(
                    error = it.message
                )
                it.printStackTrace()
            })
            .also {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    override fun setLoading(loading: Boolean) {
        _marvelDetailsViewState.value = MarvelCharacterDetailsViewState(
            isLoading = loading
        )
    }
}