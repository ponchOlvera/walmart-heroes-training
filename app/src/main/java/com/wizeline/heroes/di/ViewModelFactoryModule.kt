package com.wizeline.heroes.di

import androidx.lifecycle.ViewModelProvider
import com.wizeline.heroes.viewmodels.MarvelViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun providesViewModelFactory(factory: MarvelViewModelFactory): ViewModelProvider.Factory
}