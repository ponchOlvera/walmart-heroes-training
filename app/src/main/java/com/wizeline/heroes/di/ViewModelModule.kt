package com.wizeline.heroes.di

import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import com.wizeline.heroes.usecases.MarvelCharacterDetailsUseCase
import com.wizeline.heroes.viewmodels.IMarvelViewModel
import com.wizeline.heroes.viewmodels.MarvelDetailsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class ViewModelModule {

    @Provides
    fun provideMarvelViewModel(getMarvelCharactersUseCase: GetMarvelCharactersUseCase): IMarvelViewModel{
        return IMarvelViewModel(getMarvelCharactersUseCase)
    }

    @Provides
    fun provideMarvelDetailsViewModel(marvelCharacterDetailsUseCase: MarvelCharacterDetailsUseCase): MarvelDetailsViewModel{
        return MarvelDetailsViewModel(marvelCharacterDetailsUseCase)
    }
}