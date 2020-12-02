package com.example.e_votemvvm.Activities

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.AllBlocksAdaptor
import com.example.e_votemvvm.Models.Block
import com.example.e_votemvvm.R
import com.google.firebase.database.*

class AllBlocksActivity : AppCompatActivity() , View.OnClickListener, AllBlocksAdaptor.InterfaceBlocksAdaptorRV{


    val SHARED_PREF = "MY_SHARED_PREF"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var loadingDialog : Dialog

    lateinit var voterId:String
    lateinit var filterMyVotes:TextView
    lateinit var filterAllVotes:TextView
    lateinit var refresh: ImageView

    lateinit var blockList:ArrayList<Block>
    lateinit var myBlockList:ArrayList<Block>

    lateinit var recyclerView: RecyclerView
    var option : Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_blocks)

        refresh = findViewById(R.id.refresh_logo)
        filterAllVotes = findViewById(R.id.filter_all_votes)
        filterMyVotes = findViewById(R.id.filter_my_votes)
        refresh.setOnClickListener(this)
        filterMyVotes.setOnClickListener(this)
        filterAllVotes.setOnClickListener(this)

        filterAllVotes.setTextColor(ContextCompat.getColor(this,R.color.white))
        filterAllVotes.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
        filterMyVotes.setTextColor(ContextCompat.getColor(this,R.color.black))
        filterMyVotes.setBackgroundColor(ContextCompat.getColor(this,R.color.white))


        sharedPreferences = getSharedPreferences(SHARED_PREF , MODE_PRIVATE)

        voterId = sharedPreferences.getString("voter_id","APU1234")!!


        fillListUsingFirebase()




        blockList = ArrayList<Block>()

        myBlockList = ArrayList<Block>()


        recyclerView = findViewById(R.id.recycler_view_blocks)



    }



    fun initRecyclerView() {
        updateAdaptor()
        val gridLayoutManager = GridLayoutManager(
            this,
            2,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.setLayoutManager(gridLayoutManager)
    }

    private fun updateAdaptor(){
        val adaptor:AllBlocksAdaptor
        if(option==1)
            adaptor = AllBlocksAdaptor(this, blockList, option, voterId,this )
        else
            adaptor = AllBlocksAdaptor(this, myBlockList, option, voterId,this )
        recyclerView.adapter = adaptor
    }

    private fun showAllVoteBlocks(){
        updateAdaptor()
    }

    private fun showMyVoteBlocks(){
        updateAdaptor()
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.filter_all_votes ->{

                    option = 1
                    filterAllVotes.setTextColor(ContextCompat.getColor(this,R.color.white))
                    filterAllVotes.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))

                    filterMyVotes.setTextColor(ContextCompat.getColor(this,R.color.black))
                    filterMyVotes.setBackgroundColor(ContextCompat.getColor(this,R.color.white))

                    showAllVoteBlocks()

                }

                R.id.filter_my_votes -> {
                    option = 2
                    filterMyVotes.setTextColor(ContextCompat.getColor(this,R.color.white))
                    filterMyVotes.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
                    filterAllVotes.setTextColor(ContextCompat.getColor(this,R.color.black))
                    filterAllVotes.setBackgroundColor(ContextCompat.getColor(this,R.color.white))

                    showMyVoteBlocks()

                }

                R.id.refresh_logo -> {
                    fillListUsingFirebase()
                }
            }
        }
    }

    fun showLoadingDialog() {
        loadingDialog = Dialog(this)
        loadingDialog.setCancelable(false)
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)



        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.show()
    }

    private fun fillListUsingFirebase(){

        showLoadingDialog()

        blockList = ArrayList()
        myBlockList = ArrayList()
        blockList.clear()
        myBlockList.clear()

        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("blocks")
        val check: Query = ref.orderByChild("timestamp")

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                var block : Block
                for (child in snapshot.children) {

                    val bEncrypt = child.child("encryptedData").value.toString()
                    val bHash = child.child("hash").value.toString()
                    val bPreHash = child.child("pre_hash").value.toString()
                    val bVoterId = child.child("voter_id").value.toString()
                    val bTimeStamp = child.child("timestamp").value.toString()

                    block = Block(bHash,bPreHash,bTimeStamp,bVoterId,bEncrypt)

                    blockList.add(block)
                    if(voterId == bVoterId)
                        myBlockList.add(block)

                }
                initRecyclerView()
                loadingDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("BLOCK", "No Data")
                loadingDialog.dismiss()
            }

        })
    }

    override fun onItemClicked(position: Int, myVoteOrNot:Int) {
        if(myVoteOrNot == 4){
            Toast.makeText(applicationContext,"Locked! You can't view other's vote",Toast.LENGTH_SHORT).show()
        }
        else if(myVoteOrNot==0){
            Toast.makeText(applicationContext,"Your Vote",Toast.LENGTH_SHORT).show()

        }
    }
}