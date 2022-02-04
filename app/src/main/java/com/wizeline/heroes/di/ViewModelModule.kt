package com.wizeline.heroes.di

import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import com.wizeline.heroes.usecases.MarvelCharacterDetailsUseCase
import com.wizeline.heroes.viewmodels.MarvelDetailsViewModel
import com.wizeline.heroes.viewmodels.MarvelViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
class ViewModelModule {

    /*@Provides
    fun provideMarvelViewModel(getMarvelCharactersUseCase: GetMarvelCharactersUseCase, dispatcher: CoroutineDispatcherProvider): MarvelViewModel{
        return MarvelViewModel(getMarvelCharactersUseCase, dispatcher)
    }

    @Provides
    fun provideMarvelDetailsViewModel(marvelCharacterDetailsUseCase: MarvelCharacterDetailsUseCase): MarvelDetailsViewModel{
        return MarvelDetailsViewModel(marvelCharacterDetailsUseCase)
    }*/

    @Provides
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider{
        return CoroutineDispatcherProvider(Dispatchers.IO)
    }
}