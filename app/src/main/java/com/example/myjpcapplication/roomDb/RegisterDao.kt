package com.example.myjpcapplication.roomDb

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface RegisterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(registerSchema: RegisterSchema)

    @Update
    suspend fun updateUser(registerSchema: RegisterSchema)

    @Delete
    suspend fun deleteUser(registerSchema: RegisterSchema)

    @Query("SELECT * FROM registers ORDER BY userName DESC")
    fun getRegisters(): Flow<List<RegisterSchema>>

    @Query("SELECT * FROM registers WHERE userId==:id")
    fun getRegister(id: Int): Flow<RegisterSchema>

    @Query("SELECT * FROM registers WHERE userEmail==:email AND userPswd==:pass")
    fun getUserByEmailAndPass(email: String, pass: String): Flow<RegisterSchema?>

    @Query("UPDATE registers SET userPswd = :pass WHERE userEmail==:email")
    suspend fun updatePasswordByEmail(email: String, pass: String):Int
}