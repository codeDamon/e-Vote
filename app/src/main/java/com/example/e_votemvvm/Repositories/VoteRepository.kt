package com.example.e_votemvvm.Repositories

import androidx.lifecycle.LiveData
import com.example.e_votemvvm.Databases.VoteDao
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.Models.Vote

class VoteRepository(private val voteDao: VoteDao) {

    val getAllVotes: LiveData<List<Vote>> = voteDao.getAllVotes()

    suspend fun insert(vote: Vote){
        voteDao.insertVote(vote)
    }
}