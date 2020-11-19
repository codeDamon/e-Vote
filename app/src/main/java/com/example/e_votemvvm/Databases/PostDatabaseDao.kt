package com.example.e_votemvvm.Databases

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.e_votemvvm.Models.Post

@Dao
interface PostDatabaseDao  {

    @Insert
    suspend fun insertPost(post: Post)

    @Query("SELECT * from posts_table WHERE _id = :id")
    suspend fun getPost(id: Long): Post?

    @Query("SELECT * from posts_table ORDER BY _id ASC")
    fun getAllPosts(): LiveData<List<Post>>

    @Delete
    suspend fun deletePost(post: Post)

}