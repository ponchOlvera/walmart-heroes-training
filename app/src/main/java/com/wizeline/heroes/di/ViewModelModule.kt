package com.wizeline.heroes.di

import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
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
}