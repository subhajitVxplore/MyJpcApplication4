package com.example.myjpcapplication

import android.content.Context
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjpcapplication.daggerHilt.ValidatorrUtil
import com.example.myjpcapplication.data.repos.FakeRepository
import com.example.myjpcapplication.data.repos.Movies
import com.example.myjpcapplication.roomDb.RegisterDao
import com.example.myjpcapplication.roomDb.RegisterDatabase
import com.example.myjpcapplication.roomDb.RegisterSchema
import com.vxplore.myjpcrecyclerviewapplication.ApiService
import com.vxplore.myjpcrecyclerviewapplication.Moviee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MyViewModel @Inject constructor(
    private val validatorrUtil: ValidatorrUtil,
    private val fakeRepository: FakeRepository
) : ViewModel() {

    init {

        viewModelScope.launch {
            loadSuperheroes()
            loadSuperheroes2()
        }
    }


    fun checkEmail(context: Context,email: String){
        Toast.makeText(context, "EmailValidation : ${validatorrUtil.isEmailValid(email)}", Toast.LENGTH_SHORT).show()

    }

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName
    fun onNameChange(input: String) {
        _userName.value = input
    }

//    val notFound: LiveData<String?> = _notFound
//    private val _found = MutableLiveData()
//    val found: LiveData<RegisterSchema?> = _found
//    private val _notFound = MutableLiveData<String?>(null)
    val _found = mutableStateOf<RegisterSchema?>(null)
    val _notFound = mutableStateOf<String?>(null)
    fun readRegister(dao: RegisterDao, usrEmail: String, usrPswd: String) {
        viewModelScope.launch {
            dao.getUserByEmailAndPass(usrEmail, usrPswd).collectLatest {
                if(it != null) {
                    _found.value = it
                }
                else {
                    _notFound.value = "User Not Found"
                }
            }
        }
        // Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()

    }

    fun updatePassword(dao: RegisterDao, usrEmail: String, usrPswd: String) {
            viewModelScope.launch {
                dao.updatePasswordByEmail(usrEmail, usrPswd)
            }
            // Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
        }


    suspend fun newRegister(context: Context, usrName:String, usrEmail:String, usrPswd:String,){
        val dao = RegisterDatabase.getDatabase(context).registerDao()
        dao.addUser(RegisterSchema(userName = usrName,userEmail = usrEmail,userPswd = usrPswd))
        Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
    }


    private val _dataList = MutableStateFlow(emptyList<Person>())
    val dataList = _dataList.asStateFlow()
    private suspend fun loadSuperheroes() {
        val data =  fakeRepository.getData()
        _dataList.update {
           data
        }
    }

///////from api with AppModule
    private val _dataList2 = MutableStateFlow<Movies?>(null)
        val dataList2 = _dataList2.asStateFlow()
        private suspend fun loadSuperheroes2() {
            val movies = fakeRepository.getMoviesData()
            _dataList2.update {
               movies
            }
        }

    var movieListResponse:List<Moviee> by mutableStateOf(listOf())
    var errorMessage: String by mutableStateOf("")
    fun getMovieList() {
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val movieList = apiService.getMovies()
                movieListResponse = movieList
            }
            catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

}