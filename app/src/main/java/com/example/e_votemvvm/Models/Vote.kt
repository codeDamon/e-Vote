package com.example.e_votemvvm.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "my_votes_table")
class Vote(@ColumnInfo(name = "voter_id") var voterId:String,
           @ColumnInfo(name = "time") var time: String,
           @ColumnInfo(name = "vote_post")var votePost: String,
           @ColumnInfo(name = "vote_party")var voteParty: String) : Serializable{

    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0L
}