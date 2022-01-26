package com.wizeline.heroes.di

import com.wizeline.heroes.RetrofitRepository
import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class UseCaseModule {

    @Provides
    fun provideUseCase(@Retrofit repository: IRepository): GetMarvelCharactersUseCase{
        return GetMarvelCharactersUseCase(repository)
    }
    @Provides
    fun provideRetrofitRepository(): RetrofitRepository{
        return RetrofitRepository()
    }
}