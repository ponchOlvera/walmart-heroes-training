package com.wizeline.heroes.di

import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import com.wizeline.heroes.usecases.MarvelCharacterDetailsUseCase
import com.wizeline.heroes.viewmodels.MarvelDetailsViewModel
import com.wizeline.heroes.viewmodels.MarvelViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class ViewModelModule {

    @Provides
    fun provideMarvelViewModel(getMarvelCharactersUseCase: GetMarvelCharactersUseCase): MarvelViewModel{
        return MarvelViewModel(getMarvelCharactersUseCase)
    }

    @Provides
    fun provideMarvelDetailsViewModel(marvelCharacterDetailsUseCase: MarvelCharacterDetailsUseCase): MarvelDetailsViewModel{
        return MarvelDetailsViewModel(marvelCharacterDetailsUseCase)
    }
}