package com.wizeline.heroes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarvelViewModelFactory @Inject constructor(val getMarvelCharactersUseCase: GetMarvelCharactersUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MarvelViewModel(getMarvelCharactersUseCase) as T
    }
}