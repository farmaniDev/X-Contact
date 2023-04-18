package com.farmani.xcontact

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.farmani.xcontact.databinding.ActivityAddEditBinding
import com.farmani.xcontact.entity.ContactEntity
import com.farmani.xcontact.utilities.AvatarUtil

class AddEditActivity : AppCompatActivity() {
    var MODE_ADD = 1
    var MODE_EDIT = 2
    private var mode = MODE_ADD
    lateinit var db: AppDatabase
    private lateinit var editContact: ContactEntity
    val context = this
    private var avatarUri: Uri? = null
    private var id = 0
    private lateinit var binding: ActivityAddEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = AppDatabase.getDB(context)
        val id = intent.getIntExtra("id", 0)
        if (id == 0)
            mode = MODE_ADD
        else {
            mode = MODE_EDIT
            this.id = intent.getIntExtra("id", 0)
            editContact = db.contact().getContact(id)
        }

        init()
        listener()
    }


    private fun listener() {
        binding.btnSave.setOnClickListener {
            if (validator()) {
                val name = binding.etContactName.text.toString()
                val email =
                    if (binding.etContactEmail.text.toString() == "") null else binding.etContactEmail.text.toString()
                val number = binding.etContactNum.text.toString()
                val avatar = saveAvatar()

                if (mode == MODE_ADD)
                    addContact(name, email, number, avatar)

                if (mode == MODE_EDIT)
                    editContact(editContact.id, name, email, number, avatar)
            }
        }

        binding.ivAvatar.setOnClickListener {
            openPicker()
        }
    }

    private fun init() {
        when (mode) {
            MODE_ADD -> {
                binding.ivAvatar.setImageResource(R.drawable.no_image)
                binding.btnSave.text = getString(R.string.add)
                title = "Add New Contact"
            }

            MODE_EDIT -> {
                binding.btnSave.text = getString(R.string.edit)
                viewInitializer(editContact)
                title = "Edit ${editContact.name}"
            }
        }
    }

    private fun loadAvatar(avatar: String?) {
        if (avatar != null) {
            val avatarFile = AvatarUtil.loadAvatar(context, avatar)
            binding.ivAvatar.setImageURI(avatarFile.toUri())
        }
    }

    private fun viewInitializer(contact: ContactEntity) {
        binding.etContactName.setText(contact.name)
        binding.etContactEmail.setText(contact.email)
        binding.etContactNum.setText(contact.number)

        loadAvatar(contact.avatar)
    }

    private fun validator(): Boolean {
        return true
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                avatarUri = data!!.data!!
                changeAvatar(avatarUri!!)
            }
        }

    private fun changeAvatar(data: Uri) {
        binding.ivAvatar.setImageURI(data)
    }

    private fun saveAvatar(): String? {
        var result: String? = null
        if (avatarUri != null) {
            result = AvatarUtil.saveAvatar(context, avatarUri!!)
        }
        return result
    }

    private fun openPicker() {
        val picker = Intent(Intent.ACTION_GET_CONTENT)
        picker.type = "image/*"
        resultLauncher.launch(picker)
    }

    private fun addContact(
        contactName: String,
        email: String?,
        number: String,
        avatar: String? = null
    ) {
        val contact = ContactEntity(contactName, email, number, avatar)
        db.contact().insert(contact)
        finish()
    }

    private fun editContact(
        id: Int,
        contactName: String,
        email: String?,
        number: String,
        avatar: String? = null
    ) {

        var avatarName = avatar
        if (avatarName == null || avatarName == "")
            avatarName = editContact.avatar

        val contact = ContactEntity(id, contactName, email, number, avatarName)
        db.contact().update(contact)
        finish()
    }
}