package com.example.e_votemvvm.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                setContentView(R.layout.layout_temp_tv)
            }

        })
    }

}