package com.wizeline.heroes.di

import androidx.lifecycle.ViewModelProvider
import com.wizeline.heroes.viewmodels.MarvelDetailsViewModelFactory
import com.wizeline.heroes.viewmodels.MarvelViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
annotation class MarvelCharacters

@Qualifier
annotation class MarvelDetails


@InstallIn(SingletonComponent::class)
@Module
abstract class ViewModelFactoryModule {

    @MarvelCharacters
    @Binds
    abstract fun providesCharactersViewModelFactory(factory: MarvelViewModelFactory): ViewModelProvider.Factory


    @MarvelDetails
    @Binds
    abstract fun providesDetailsViewModelFactory(factory: MarvelDetailsViewModelFactory): ViewModelProvider.Factory
}