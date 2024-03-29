package com.adtarassov.audioplayer.di

import com.adtarassov.audioplayer.data.api.AudioBackendApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

  @Provides
  @Singleton
  fun provideRetrofit(): Retrofit =
    Retrofit.Builder()
      .baseUrl(AudioBackendApi.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

  @Provides
  @Singleton
  fun providePlaceholderApi(retrofit: Retrofit): AudioBackendApi =
    retrofit.create(AudioBackendApi::class.java)
}