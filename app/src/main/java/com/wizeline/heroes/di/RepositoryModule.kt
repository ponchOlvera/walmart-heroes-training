package com.wizeline.heroes.di

import com.wizeline.heroes.RetrofitRepository
import com.wizeline.heroes.interfaces.IRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
annotation class Retrofit

@InstallIn(SingletonComponent::class)
@Module
class RetrofitRepository{
}

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Retrofit
    @Binds
    abstract fun bindRetrofitRepository(retrofitRepository: RetrofitRepository): IRepository
}