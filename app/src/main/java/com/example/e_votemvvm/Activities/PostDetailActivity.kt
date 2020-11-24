package com.example.e_votemvvm.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.PartyShortAdaptorRV
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.R
import com.example.e_votemvvm.ViewModels.PostViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostDetailActivity : AppCompatActivity(), View.OnClickListener {

    var post:Post? = null
    lateinit var viewModel: PostViewModel
    var list: ArrayList<Party> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        post  = intent.getSerializableExtra("post") as? Post



            if(post == null){
                Toast.makeText(this, "Null recieved", Toast.LENGTH_SHORT).show()
            }

            else {
                val gson = Gson()
                val type = object : TypeToken<ArrayList<Party>>(){}.type
                val partyList:ArrayList<Party> = gson.fromJson(post!!.parties,type)


                val textView: TextView = findViewById(R.id.title)
                textView.text = post!!.postName
                val heading : TextView = findViewById(R.id.post_name)
                heading.text = post!!.postName
                val startDate : TextView = findViewById(R.id.post_date)
                startDate.text = post!!.postStart
                val endDate : TextView = findViewById(R.id.post_end)
                endDate.text = post!!.postEnd


                val detail:TextView = findViewById(R.id.details)
                detail.movementMethod = ScrollingMovementMethod()
                detail.text = post!!.details

                for(i in partyList)
                    list.add(i)

                initRV()

                val voteBtn = findViewById<Button>(R.id.vote_btn)
                voteBtn.setOnClickListener(this)


            }
    }

    private fun initRV(){
        val adapter = PartyShortAdaptorRV(this,list)
        val recycleView = findViewById<RecyclerView>(R.id.parties_rv)
        recycleView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycleView.adapter = adapter
        recycleView.setHasFixedSize(true)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){

                R.id.vote_btn -> {

                    intent = Intent(this,VoteActivity::class.java)
                    intent.putExtra("post",post)
                    startActivity(intent) }



            }
        }
    }
}