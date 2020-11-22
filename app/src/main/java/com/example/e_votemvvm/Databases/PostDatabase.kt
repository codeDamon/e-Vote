package com.example.e_votemvvm.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.Models.Vote

@Database(entities = [Post::class, Vote::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {

    abstract fun getPostDao() : PostDatabaseDao
    abstract fun getVoteDao() : VoteDao

    companion object {

        @Volatile
        private var INSTANCE: PostDatabase? = null

        fun getInstance(context: Context): PostDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PostDatabase::class.java,
                        "post_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}