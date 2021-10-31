package com.ewake.restclient.presentation.di.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        builder.addInterceptor(loggingInterceptor)

        return builder.build()
    }
}