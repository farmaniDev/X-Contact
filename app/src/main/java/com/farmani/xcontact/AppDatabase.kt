package com.farmani.xcontact

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farmani.xcontact.dao.UserDAO
import com.farmani.xcontact.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun user(): UserDAO

    companion object {
        private var instance: AppDatabase? = null
        private const val DB_NAME = "contacts_db"

        fun getDB(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}