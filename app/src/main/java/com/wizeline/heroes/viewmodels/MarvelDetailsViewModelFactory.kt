package com.wizeline.heroes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizeline.heroes.usecases.MarvelCharacterDetailsUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarvelDetailsViewModelFactory @Inject constructor(val marvelCharacterDetailsUseCase: MarvelCharacterDetailsUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MarvelDetailsViewModel(marvelCharacterDetailsUseCase) as T
    }
}