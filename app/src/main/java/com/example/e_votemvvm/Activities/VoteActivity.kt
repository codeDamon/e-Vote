package com.example.e_votemvvm.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.MyVoteAdaptorRV
import com.example.e_votemvvm.Adaptors.PartyShortAdaptorRV
import com.example.e_votemvvm.Adaptors.PartyVoteAdaptorRV
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.Models.Vote
import com.example.e_votemvvm.R
import com.example.e_votemvvm.ViewModels.PostViewModel
import com.example.e_votemvvm.ViewModels.VoteViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VoteActivity : AppCompatActivity() , PartyVoteAdaptorRV.InterfacePartyAdaptorRV, View.OnClickListener{

    var list: ArrayList<Party> = ArrayList()
    var selectedPartyIndex : Int = -1
    var thisPost : String = ""
    var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        post = intent.getSerializableExtra("post") as? Post

        if(post == null){
            Toast.makeText(this, "Null recieved", Toast.LENGTH_SHORT).show()
        }
        else {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Party>>(){}.type
            val partyList:ArrayList<Party> = gson.fromJson(post!!.parties,type)

            for(i in partyList)
                list.add(i)

            initRV()
            val cancelBtn = findViewById<ImageView>(R.id.cancel_btn)
            val acceptBtn = findViewById<ImageView>(R.id.accept_btn)
            val postName = findViewById<TextView>(R.id.post_name)
            val profileLogo = findViewById<ImageView>(R.id.profile_logo)
            postName.text = post!!.postName

            thisPost=post!!.postName

            cancelBtn.setOnClickListener(this)
            acceptBtn.setOnClickListener(this)
            profileLogo.setOnClickListener(this)
        }
    }

    private fun initRV(){
        val adapter = PartyVoteAdaptorRV(this,list,this)

        val recycleView = findViewById<RecyclerView>(R.id.parties_rv)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter
        recycleView.setHasFixedSize(true)
    }

    override fun onItemClicked(party: Party,position:Int) {

        if(position!=-1)
        Toast.makeText(this, party.partyName+" Selected", Toast.LENGTH_SHORT).show()
        selectedPartyIndex = position
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.accept_btn -> {
                if(selectedPartyIndex == -1)
                    Toast.makeText(this, "No party selected", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(this, "Processing Request...", Toast.LENGTH_SHORT).show()

                    val viewModel: PostViewModel = ViewModelProvider(
                        this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                            application)).get(PostViewModel::class.java)

                    post?.let { viewModel.deletePost(it) }


                    val vote =Vote("ABCD 123456", "24 Jan 2020 34:00", thisPost,list[selectedPartyIndex].partyName)

                    val viewModelMyVote: VoteViewModel = ViewModelProvider(this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(VoteViewModel::class.java)

                    viewModelMyVote.insertVote(vote)

                }

            }
            R.id.cancel_btn -> {
                finish()
            }

            R.id.profile_logo -> {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                val intent2 = Intent(this,MyVotesActivity::class.java)
                startActivity(intent2)
                finishAffinity()
            }
        }
    }
}