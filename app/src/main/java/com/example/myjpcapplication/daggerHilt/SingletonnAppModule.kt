package com.example.myjpcapplication.daggerHilt

import com.example.myjpcapplication.data.repos.FakeRepository
import com.example.myjpcapplication.data.repos.MovieApi
import com.example.myjpcapplication.data.repos.Movies
import com.example.myjpcapplication.repo_impls.FakeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonnAppModule {
    @Binds
    abstract fun bindValidation(impl: ValidatorrImpl): ValidatorrUtil

    @Binds
    abstract fun bindFakeRepo(impl: FakeRepositoryImpl): FakeRepository

    companion object {

        private fun <T> provideApi(klass: Class<T>): T {
            return Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(klass)
        }

        @Provides
        @Singleton
        fun provideMovies(): MovieApi = provideApi(MovieApi::class.java)
    }

}