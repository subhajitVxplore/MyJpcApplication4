package com.example.myjpcapplication.data.repos

import android.app.Person
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("popular?")
    suspend fun getPopularMovies(@Query("api_key") api_key : String) : Movies
}