package com.example.e_votemvvm.Models

import android.icu.util.Output
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.security.PrivateKey
import java.sql.Blob
import java.sql.Types.BLOB
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "posts_table")
data class Post(@ColumnInfo(name = "post_name") var postName : String,
                @ColumnInfo(name = "post_start") var postStart : String,
                @ColumnInfo(name = "post_end") var postEnd : String,
                @ColumnInfo(name="post_parties") var parties : String,
                @ColumnInfo(name = "post_details")var details: String

                ) : Serializable
{
    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0L
}