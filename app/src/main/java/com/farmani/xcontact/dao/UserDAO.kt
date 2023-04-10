package com.farmani.xcontact.dao

import androidx.room.*
import com.farmani.xcontact.entity.UserEntity

@Dao
interface UserDAO {
    @Query("select * from users")
    fun getAll(): List<UserEntity>

    @Insert
    fun insert(user: UserEntity)

    @Update
    fun update(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("delete from users")
    fun deleteAll()
}