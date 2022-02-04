package com.wizeline.heroes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
class ViewModelModule {
    @Provides
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider{
        return CoroutineDispatcherProvider(Dispatchers.IO)
    }
}