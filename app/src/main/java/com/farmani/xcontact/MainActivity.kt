package com.farmani.xcontact

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.farmani.xcontact.databinding.ActivityMainBinding
import com.farmani.xcontact.entity.UserEntity
import com.farmani.xcontact.utilities.SaveAvatar

//import com.farmani.xcontact.utilities.SaveAvatar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var db: AppDatabase
    private val context = this
    val requestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDB(context)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
//            val user1 = UserEntity("Mohammad Javad", "test@gmail.com", "09111234567", null)
//            db.user().insert(user1)
            advancedAddUser()
        }

    }


    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                val avatarUri = data!!.data!!
                val saveAvatar = SaveAvatar.saveAvatar(context, avatarUri)

                addUser("MJ Farmani", null, "09396838975", saveAvatar)
            }
        }

    private fun advancedAddUser() {
        val picker = Intent(Intent.ACTION_GET_CONTENT)
        picker.type = "image/*"
        resultLauncher.launch(picker)
    }

    private fun addUser(name: String, email: String?, phone: String, avatar: String?) {
        db.user().insert(UserEntity(name, email, phone, avatar))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}