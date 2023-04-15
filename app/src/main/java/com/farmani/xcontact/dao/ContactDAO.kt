package com.farmani.xcontact.dao

import androidx.room.*
import com.farmani.xcontact.entity.ContactEntity

@Dao
interface ContactDAO {
    @Query("select * from contacts")
    fun getAll(): List<ContactEntity>

    @Query("select * from contacts where id = :id")
    fun getContact(id: Int) : ContactEntity

    @Insert
    fun insert(contact: ContactEntity)

    @Update
    fun update(contact: ContactEntity)

    @Delete
    fun delete(contact: ContactEntity)

    @Query("delete from contacts")
    fun deleteAll()
}