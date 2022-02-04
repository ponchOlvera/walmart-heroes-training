package com.wizeline.heroes.di

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CoroutineDispatcherProvider @Inject constructor(val name: CoroutineDispatcher)