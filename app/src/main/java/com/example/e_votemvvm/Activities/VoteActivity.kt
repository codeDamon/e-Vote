package com.example.e_votemvvm.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.PartyShortAdaptorRV
import com.example.e_votemvvm.Adaptors.PartyVoteAdaptorRV
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VoteActivity : AppCompatActivity() , PartyVoteAdaptorRV.InterfacePartyAdaptorRV, View.OnClickListener{

    var list: ArrayList<Party> = ArrayList()
    var selectedPartyIndex : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        val post : Post? = intent.getSerializableExtra("post") as? Post

        if(post == null){
            Toast.makeText(this, "Null recieved", Toast.LENGTH_SHORT).show()
        }
        else {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Party>>(){}.type
            val partyList:ArrayList<Party> = gson.fromJson(post.parties,type)

            for(i in partyList)
                list.add(i)

            initRV()
            val cancelBtn = findViewById<ImageView>(R.id.cancel_btn)
            val acceptBtn = findViewById<ImageView>(R.id.accept_btn)
            cancelBtn.setOnClickListener(this)
            acceptBtn.setOnClickListener(this)
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

        Toast.makeText(this, party.partyName+" Selected", Toast.LENGTH_SHORT).show()
        selectedPartyIndex = position
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.accept_btn -> {
                if(selectedPartyIndex == -1)
                    Toast.makeText(this, "No party selected", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Processing Request...", Toast.LENGTH_SHORT).show()

            }
            R.id.cancel_btn -> {
                finish()
            }
        }
    }
}