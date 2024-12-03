package com.example.hyperdesigntask.di

import android.content.Context
import com.example.hyperdesigntask.data.local.TokenManager
import com.example.hyperdesigntask.data.repo.AuthRepo
import com.example.hyperdesigntask.networking.AuthService
import com.example.hyperdesigntask.networking.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApi(): AuthService {
        return RetrofitInstance.retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepo(
        api: AuthService,
        tokenManager: TokenManager
    ): AuthRepo {
        return AuthRepo(api, tokenManager)
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context,
    ): TokenManager {
        return TokenManager(context)
    }


}