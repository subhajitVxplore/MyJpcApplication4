package com.example.myjpcapplication.repo_impls

import android.util.Log
import com.example.myjpcapplication.Person
import com.example.myjpcapplication.data.repos.FakeRepository
import com.example.myjpcapplication.data.repos.MovieApi
import com.example.myjpcapplication.data.repos.Movies
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class FakeRepositoryImpl @Inject constructor(
    private val movies: MovieApi
) : FakeRepository {

    override suspend fun getData(): List<Person> {
        delay(2000L)


        return listOf(
            Person("Grace Hopper", 25),
            Person("Ada Lovelace", 29),
            Person("John Smith", 28),
            Person("Elon Musk", 41),
            Person("Will Smith", 31),
            Person("Robert James", 42),
            Person("Anthony Curry", 91),
            Person("Kevin Jackson", 22),
            Person("Robert Curry", 1),
            Person("John Curry", 9),
            Person("Ada Jackson", 2),
            Person("Joe Defoe", 35)
        )
    }

    override suspend fun getMoviesData(): Movies? {
        return try {
            val result = movies.getPopularMovies("69d66957eebff9666ea46bd464773cf0")
            return result
        } catch (ex: HttpException) {
            Log.d("API_RESPONSE", "${ex.message()}")
             null
        } catch (ex: IOException) {
            Log.d("API_RESPONSE", "${ex.message}")
          null
        } catch (ex: Exception) {
            Log.d("API_RESPONSE", "${ex.message}")
            null
        }
    }

}