package com.ibrahim.madarsoft_task.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharingModule {

    @Provides
    @Singleton
    fun provideSharingStarted(): SharingStarted = SharingStarted.WhileSubscribed(5000)
}

