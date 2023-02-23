package com.example.myjpcapplication.data.repos

import com.example.myjpcapplication.Person
import retrofit2.Response

interface FakeRepository {
    suspend fun getData(): List<Person>
    suspend fun getMoviesData():Movies?

}



