package com.example.myjpcapplication.roomDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "registers")
data class RegisterSchema(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "userEmail") val userEmail: String,
    @ColumnInfo(name = "userPswd") val userPswd: String
    )
