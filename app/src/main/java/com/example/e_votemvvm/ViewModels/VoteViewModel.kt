package com.example.e_votemvvm.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.e_votemvvm.Databases.PostDatabase
import com.example.e_votemvvm.Models.Vote
import com.example.e_votemvvm.Repositories.VoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoteViewModel(application: Application):AndroidViewModel(application) {

    val allVotes: LiveData<List<Vote>>
    private val repository: VoteRepository

    init {
        val dao = PostDatabase.getInstance(application).getVoteDao()
        repository = VoteRepository(dao)
        allVotes = repository.getAllVotes

    }

    fun insertVote(vote: Vote) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(vote)
    }

    fun checkForVoted(postVoted:String):Long {
        var i : Long = 0
        viewModelScope.launch(Dispatchers.IO){
         i = repository.checkVotedOrNot(postVoted)}
        return i
    }

    fun deleteAllVotes() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }

}