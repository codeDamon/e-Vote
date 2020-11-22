package com.example.e_votemvvm.Databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.e_votemvvm.Models.Vote

@Dao
interface VoteDao {

    @Insert
    suspend fun insertVote(vote: Vote)

    @Query("SELECT * from my_votes_table ORDER BY _id DESC")
    fun getAllVotes(): LiveData<List<Vote>>

}