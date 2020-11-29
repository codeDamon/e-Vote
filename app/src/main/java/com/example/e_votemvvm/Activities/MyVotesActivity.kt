package com.example.e_votemvvm.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.MyVoteAdaptorRV
import com.example.e_votemvvm.Adaptors.PostAdaptorRV
import com.example.e_votemvvm.R
import com.example.e_votemvvm.ViewModels.VoteViewModel

class MyVotesActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_votes)

        val adapter = MyVoteAdaptorRV(this)
        val recyclerView = findViewById<RecyclerView>(R.id.my_votes_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        val viewModelMyVote: VoteViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(VoteViewModel::class.java)

        viewModelMyVote.allVotes.observe(this,{ list ->
            list?.let { adapter.updateList(it) }

            if(list.isEmpty()){
                setContentView(R.layout.layout_no_my_votes)
                val voteBtn = findViewById<Button>(R.id.voteNowBtn)
                voteBtn.setOnClickListener {
                    val intent2 = Intent(this, MainActivity::class.java)
                    startActivity(intent2)
                    finishAffinity()
                }
            }

        })
    }

}