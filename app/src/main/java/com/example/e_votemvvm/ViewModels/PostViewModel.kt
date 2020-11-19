package com.example.e_votemvvm.ViewModels

import android.app.Application
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.e_votemvvm.Databases.PostDatabase
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.Repositories.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {

    val allPosts: LiveData<List<Post>>
    var post: Post? = null

    private val repository: PostRepository

    init {
        val dao = PostDatabase.getInstance(application).getPostDao()
        repository = PostRepository(dao)
        allPosts = repository.getAllPosts

    }

    fun insertPost(post: Post) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(post)
    }

    fun deletePost(post: Post) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(post)
    }

    fun getPost(id: Long) :Post? {
        viewModelScope.launch(Dispatchers.IO){
             post=repository.getPost(id)
        }
        return post
    }

}