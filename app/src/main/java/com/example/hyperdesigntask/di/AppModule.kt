package com.example.hyperdesigntask.di

import android.content.Context
import android.util.Log
import com.example.hyperdesigntask.data.local.TokenManager
import com.example.hyperdesigntask.data.repo.AuthRepo
import com.example.hyperdesigntask.networking.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
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



        @Provides
        @Singleton
        fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    val token = tokenManager.getAccessToken()
                    Log.i("Token", "provideOkHttpClient: "+token)
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                    chain.proceed(requestBuilder.build())
                }
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://www.hyper-design.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        @Provides
        @Singleton
        fun provideAuthService(retrofit: Retrofit): AuthService {
            return retrofit.create(AuthService::class.java)
        }
    }
