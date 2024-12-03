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

                // Log request URL
                Log.i("refresh", "Request URL: ${originalRequest.url}")

                // Log HTTP Method
                Log.i("refresh", "HTTP Method: ${originalRequest.method}")

                // Get token from TokenManager
                val token = tokenManager.getAccessToken()
                Log.i("refresh", "Access Token Retrieved: $token")

                // Conditionally add Authorization Header
                if (!originalRequest.url.toString().contains("refresh")) {
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                        Log.i("refresh", "Authorization Header Added")
                    } else {
                        Log.w("refresh", "No Authorization Token Found")
                    }
                } else {
                    Log.i("refresh", "No Authorization Header Needed for Refresh Endpoint")
                }

                // Log Headers
                Log.i("refresh", "Request Headers: ${originalRequest.headers}")

                // Build modified request
                val modifiedRequest = requestBuilder.build()
                val response = chain.proceed(modifiedRequest)

                // Log Response
                Log.i("refresh", "Response Code: ${response.code}")
                Log.i("refresh", "Response Message: ${response.message}")

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
}