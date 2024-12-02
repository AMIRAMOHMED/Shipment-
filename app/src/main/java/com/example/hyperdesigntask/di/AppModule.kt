package com.example.hyperdesigntask.di

import com.example.hyperdesigntask.data.repo.AuthRepo
import com.example.hyperdesigntask.networking.HyperDesginTaskService
import com.example.hyperdesigntask.networking.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApi(): HyperDesginTaskService {
        return RetrofitInstance.retrofit.create(HyperDesginTaskService::class.java)
    }


    @Provides
    @Singleton
    fun provideAuthRepo(api: HyperDesginTaskService): AuthRepo {
        return AuthRepo(api)
    }
}