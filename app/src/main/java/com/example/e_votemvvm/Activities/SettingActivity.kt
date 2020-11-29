package com.example.e_votemvvm.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.preference.*
import com.example.e_votemvvm.Fragments.SettingsFragment
import com.example.e_votemvvm.R
import com.example.e_votemvvm.ViewModels.PostViewModel

class SettingActivity : AppCompatActivity() ,Preference.OnPreferenceChangeListener {

    val SHARED_PREF = "MY_SHARED_PREF"
    lateinit var sharedPreferences: SharedPreferences




    lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar1)
        setSupportActionBar(toolbar)

        val fragmentManager: FragmentManager = this.supportFragmentManager
        val fragTransaction = fragmentManager.beginTransaction()
        fragTransaction.replace(R.id.frame_layout, SettingsFragment())
        fragTransaction.commit()

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application
            )
        )
            .get(PostViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_activity_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.my_vote_menu -> {
                intent = Intent(this, MyVotesActivity::class.java)
                startActivity(intent)

            }

            R.id.logout_menu -> {
                Toast.makeText(applicationContext, "Logout Successful", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().clear().apply()
                viewModel.deleteAllPosts()
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()

            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        if(preference is SwitchPreference){
            Toast.makeText(applicationContext, "YES", Toast.LENGTH_SHORT).show()

        }
        else
        {
            Toast.makeText(applicationContext, "NO", Toast.LENGTH_SHORT).show()

        }
        return true
    }
}