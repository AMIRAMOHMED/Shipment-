package com.example.hyperdesigntask.di

import android.content.Context
import android.util.Log
import com.example.hyperdesigntask.data.local.TokenManager
import com.example.hyperdesigntask.data.repo.AuthRepo
import com.example.hyperdesigntask.data.networking.AuthService
import com.example.hyperdesigntask.data.networking.ShipmentService
import com.example.hyperdesigntask.data.repo.ShipmentRepo
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
    fun provideShipmentRepository(api: ShipmentService): ShipmentRepo {
        return ShipmentRepo(api)
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context
    ): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                val token = tokenManager.getAccessToken()
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                        Log.i("refresh", "Authorization Header Added")
                }
                val modifiedRequest = requestBuilder.build()
                val response = chain.proceed(modifiedRequest)
                response
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


    @Provides
    @Singleton
    fun provideShipmentService(retrofit: Retrofit): ShipmentService {
        return retrofit.create(ShipmentService::class.java)
    }
}