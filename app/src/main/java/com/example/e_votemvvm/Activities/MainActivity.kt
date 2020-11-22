package com.example.e_votemvvm.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.PostAdaptorRV
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.R
import com.example.e_votemvvm.ViewModels.PostViewModel
import com.google.gson.Gson


class MainActivity : AppCompatActivity() , PostAdaptorRV.InterfacePostAdaptorRV , View.OnClickListener{

    lateinit var viewModel: PostViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_posts)
        val adapter = PostAdaptorRV(this, this)

        val profileLogo = findViewById<ImageView>(R.id.profile_logo)
        profileLogo.setOnClickListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application
            )
        )
                .get(PostViewModel::class.java)

        viewModel.allPosts.observe(this, Observer { list ->
            list?.let { adapter.updateList(it) }

        })


        val sharedPreferences = getSharedPreferences("mySharePref", MODE_PRIVATE)
        val newUser = sharedPreferences.getBoolean("newUser",true)
        val myEdit = sharedPreferences.edit()

        if(newUser) {
            myEdit.putBoolean("newUser", false).apply()
            fillList()
        }
    }

    private fun fillList(){


        val parties: ArrayList<Party> = ArrayList()

        var url = "https://4.bp.blogspot.com/-maWjx4TelnU/TjKM45tgl5I/AAAAAAAAAKE/TE0yMkDU0GM/s1600/BJP_logo.jpg"
        var partyTemp : Party = Party("Bharatiya Janata Party", url, "Narendra Modi", "BJP", false)
        parties.add(partyTemp)

        url="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Falochonaa.files.wordpress.com%2F2014%2F02%2Fcongress-logo.jpg&f=1&nofb=1"
        partyTemp = Party("Indian National Congress", url, "Sonia Gandhi", "INC", false)
        parties.add(partyTemp)

        url="https://images.news18.com/ibnlive/uploads/2016/10/aap.jpg?impolicy=website&width=536&height=356"
        partyTemp = Party("Aam Aadami Party", url, "Arvind Kejriwal", "AAP", false)
        parties.add(partyTemp)

        url="https://pbs.twimg.com/profile_images/497400199333965824/2FwsZJrK.jpeg"
        partyTemp = Party("Shiv Sena", url, "Uddav Thackeray", "SS", false)
        parties.add(partyTemp)
        parties.add(partyTemp)
        parties.add(partyTemp)

        val gson = Gson();
        val pp: String = gson.toJson(parties)



        var postObj: Post = Post(
            "Lok Sabha Election", "23 Jan 2021 11:00",
            "24 Jan 2021 18:00", pp, "details_here"
        )
        viewModel.insertPost(postObj)


        postObj = Post(
            "State Assembly Election", "30 Jan 2021 06:00",
            "31 Jan 2021 06:00", pp, "details_here"
        )
        viewModel.insertPost(postObj)


        postObj = Post(
            "Panchayat Election", "23 Feb 2021 08:00",
            "24 Feb 2021 08:00", pp, "details_here"
        )
        viewModel.insertPost(postObj)

        postObj = Post(
            "Municipal Corporation Election", "23 Jan 2021 00:00",
            "24 Jan 2021 07:59", pp, "details_here"
        )
        viewModel.insertPost(postObj)
    }

    override fun onItemClicked(post: Post) {
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("post", post)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.profile_logo -> {
                val intent = Intent(this,TestActivity::class.java)
                startActivity(intent)
            }
        }
    }
}