package com.example.e_votemvvm.Activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.MyVoteAdaptorRV
import com.example.e_votemvvm.Adaptors.PostAdaptorRV
import com.example.e_votemvvm.R
import com.example.e_votemvvm.ViewModels.VoteViewModel

class MyVotesActivity : AppCompatActivity() {

    val SHARED_PREF = "MY_SHARED_PREF"
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_votes)

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)

        val noVoteListLayout = findViewById<ConstraintLayout>(R.id.const_no_list)
        val voteListLayout = findViewById<ConstraintLayout>(R.id.const_have_list)

        voteListLayout.visibility = View.VISIBLE
        noVoteListLayout.visibility = View.GONE



        val adapter = MyVoteAdaptorRV(this)
        val recyclerView = findViewById<RecyclerView>(R.id.my_votes_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        val viewModelMyVote: VoteViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(VoteViewModel::class.java)

        viewModelMyVote.allVotes.observe(this,{ list ->
            list?.let { adapter.updateList(it) }

            if(list.isEmpty()){
               // setContentView(R.layout.layout_no_my_votes)
                   voteListLayout.visibility = View.GONE
                    noVoteListLayout.visibility = View.VISIBLE
                val voteBtn = findViewById<Button>(R.id.voteNowBtn)
                voteBtn.setOnClickListener {
                    val intent2 = Intent(this, MainActivity::class.java)
                    startActivity(intent2)
                    finishAffinity()
                }
            }



        })
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    fun verify(view: View) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_vote_activity_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.verify_vote_menu -> {
                intent = Intent(this,AllBlocksActivity::class.java)
                startActivity(intent)

            }
            R.id.settings_menu -> {
                intent = Intent(this,SettingActivity::class.java)
                startActivity(intent)

            }
            R.id.logout_menu -> {
                Toast.makeText(applicationContext, "Logout Successful", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().clear().apply()

                //viewModel.deleteAllPosts()
                intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()

            }
        }
        return super.onOptionsItemSelected(item)
    }

}