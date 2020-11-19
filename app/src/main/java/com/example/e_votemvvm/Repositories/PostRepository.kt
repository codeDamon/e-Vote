package com.example.e_votemvvm.Repositories

import androidx.lifecycle.LiveData
import com.example.e_votemvvm.Databases.PostDatabaseDao
import com.example.e_votemvvm.Models.Post

class PostRepository(private val postDatabaseDao: PostDatabaseDao) {

    val getAllPosts: LiveData<List<Post>> = postDatabaseDao.getAllPosts()

    suspend fun insert(post: Post){
        postDatabaseDao.insertPost(post)
    }

    suspend fun delete(post: Post){
        postDatabaseDao.deletePost(post)
    }

    suspend fun getPost(id: Long):Post?{
        return postDatabaseDao.getPost(id)
    }

}