package com.farmani.xcontact.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
class ContactEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "contact_name")
    var name: String,
    @ColumnInfo(name = "contact_email")
    var email: String?,
    @ColumnInfo(name = "contact_number")
    var number: String,
    @ColumnInfo(name = "contact_avatar")
    var avatar: String? = null
) {
    constructor(name: String, email: String?, number: String, avatar: String?) : this(0, name, email, number, avatar)
}