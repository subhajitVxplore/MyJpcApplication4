package com.example.myjpcapplication.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [RegisterSchema::class], version = 1, exportSchema = true)
abstract class RegisterDatabase : RoomDatabase(){
    abstract fun registerDao(): RegisterDao
    companion object {
        @Volatile
        private var INSTANCE: RegisterDatabase? = null

        fun getDatabase(context: Context): RegisterDatabase {
            if (INSTANCE == null) { // if the INSTANCE is not null, then return it,// if it is, then create the database
                synchronized(this) {INSTANCE = buildDatabase(context)}
            }
            return INSTANCE!!
        }
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE registers ADD COLUMN lastUpdate INTEGER NOT NULL DEFAULT 0")// this query will add a new column database
            }
        }
        private fun buildDatabase(context: Context): RegisterDatabase {
            return Room.databaseBuilder(context.applicationContext,RegisterDatabase::class.java,"registers_database" ).build()//.addMigrations(MIGRATION_1_2)
        }
    }
}