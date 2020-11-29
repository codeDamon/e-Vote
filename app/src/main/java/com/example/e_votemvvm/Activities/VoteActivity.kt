package com.example.e_votemvvm.Activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.PartyVoteAdaptorRV
import com.example.e_votemvvm.Models.Block
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.Models.Vote
import com.example.e_votemvvm.R
import com.example.e_votemvvm.Utilities.BiometricVerification
import com.example.e_votemvvm.Utilities.HashSHA256
import com.example.e_votemvvm.ViewModels.PostViewModel
import com.example.e_votemvvm.ViewModels.VoteViewModel
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VoteActivity : AppCompatActivity() , PartyVoteAdaptorRV.InterfacePartyAdaptorRV,
    BiometricVerification.OnVerificationStateChangeListener,View.OnClickListener {

    val SHARED_PREF = "MY_SHARED_PREF"
    lateinit var sharedPreferences: SharedPreferences

    lateinit var loadingDialog : Dialog

    lateinit var rootNode: FirebaseDatabase
    lateinit var votesRef : DatabaseReference

    //fields for a block
    lateinit var dateTime : String
    lateinit var voterId: String
    lateinit var partyVoted: String
    lateinit var postVoted: String
    lateinit var previousHash: String


    var list: ArrayList<Party> = ArrayList()
    var selectedPartyIndex : Int = -1
    var thisPost : String = ""
    var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        val cancelBtn = findViewById<ImageView>(R.id.cancel_btn)
        val acceptBtn = findViewById<ImageView>(R.id.accept_btn)
        val postName = findViewById<TextView>(R.id.post_name)
        val profileLogo = findViewById<ImageView>(R.id.profile_logo)


        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        voterId = sharedPreferences.getString("voter_id", "APU1234").toString()


        post = intent.getSerializableExtra("post") as? Post

        if(post == null){
            Toast.makeText(this, "Null recieved", Toast.LENGTH_SHORT).show()
        }
        else {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Party>>(){}.type
            val partyList:ArrayList<Party> = gson.fromJson(post!!.parties, type)

            for(i in partyList)
                list.add(i)

            initRV()
            postName.text = post!!.postName
            thisPost = post!!.postName

            cancelBtn.setOnClickListener(this)
            acceptBtn.setOnClickListener(this)
            profileLogo.setOnClickListener(this)
        }
    }

    private fun initRV(){
        val adapter = PartyVoteAdaptorRV(this, list, this)

        val recycleView = findViewById<RecyclerView>(R.id.parties_rv)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter
        recycleView.setHasFixedSize(true)
    }

    override fun onItemClicked(party: Party, position: Int) {

        if(position!=-1)
        //Toast.makeText(this, party.partyName + " Selected", Toast.LENGTH_SHORT).show()
        selectedPartyIndex = position
    }

    private fun showLoadingDialog() {
        loadingDialog = Dialog(this)
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        loadingDialog.setCancelable(true)

        loadingDialog.setContentView(R.layout.loading_success_layout)
        loadingDialog.show()
    }

    private fun voteNow(){

        //Toast.makeText(this, "Processing Request...", Toast.LENGTH_SHORT).show()
        showLoadingDialog()

        val viewModel: PostViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application
            )
        ).get(PostViewModel::class.java)

        post?.let { viewModel.deletePost(it) }

        val sdf = SimpleDateFormat("dd-MMMM,yyyy-HH:mm")
        val c: Calendar = Calendar.getInstance()

        dateTime = sdf.format(c.time)
        partyVoted = list[selectedPartyIndex].partyName
        postVoted = thisPost

        val vote = Vote(
            voterId,
            dateTime,
            postVoted,
            partyVoted
        )

        addToMyVotes(vote)
        addVoteToFirebase(voterId, dateTime, vote)
        saveBlockOnFirebase()


        selectedPartyIndex = -1


    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.accept_btn -> {
                if (selectedPartyIndex == -1)
                    Toast.makeText(this, "No party selected", Toast.LENGTH_SHORT).show()
                else {

                    if (sharedPreferences.getBoolean("isBioVerificationEnabled", false)) {

                        val biometricVerification = BiometricVerification(this,this)
                        if(biometricVerification.checkBiometricSupport()) {
                            biometricVerification.buildBiometricPrompt(
                                "Biometric Verification",
                                "Please Verify",
                                "Cancel"
                            )
                        }
                    }
                    else{
                        voteNow()
                    }
                }

            }
            R.id.cancel_btn -> {
                finish()
            }

            R.id.profile_logo -> {

                val intent2 = Intent(this, MyVotesActivity::class.java)
                startActivity(intent2)
               
            }
        }
    }

    private fun addToMyVotes(vote: Vote){

        val viewModelMyVote: VoteViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(VoteViewModel::class.java)

        viewModelMyVote.insertVote(vote)
    }

    private fun addVoteToFirebase(voterId: String, currentDate: String, vote: Vote){

        rootNode = FirebaseDatabase.getInstance();
        votesRef = rootNode.getReference("votes")

        votesRef.child(voterId).child(currentDate).setValue(vote)

    }

    private fun getHash(data: String) : String? {

        var hash : String? = null
        try {
            val hashObj  = HashSHA256()
            hash = hashObj.toHexString(hashObj.getSHA(data))

        }
        catch (e: NoSuchAlgorithmException){
            Log.d("getHash", e.toString())
        }
        return hash

    }

    private fun saveBlockOnFirebase(){

        //get hash for the block
        val hash = getHash(voterId + dateTime + postVoted + partyVoted)
        if(hash!=null){
            Log.d("BLOCK", hash)
        }
        else{
            Toast.makeText(this, "Hashing Failure", Toast.LENGTH_SHORT).show()
            return
        }


        //fetch hash of last block from Firebase
        val ref : DatabaseReference = FirebaseDatabase.getInstance().reference.child("blocks")
        val check : Query = ref.orderByChild("timestamp").limitToLast(1)

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    for (child in p0.children) {
                        Log.d("BLOCK", child.key!!)
                        previousHash = child.key!!
                    }

                    //fetching successful

                    //creating newBlock
                    val time = System.currentTimeMillis().toString()
                    val newBlock = Block(hash, previousHash, time , voterId)

                    //uploading the block to Firebase
                    rootNode = FirebaseDatabase.getInstance();
                    votesRef = rootNode.getReference("blocks")

                    votesRef.child(hash).setValue(newBlock)

                    //Log.d("BLOCK", previousHash)
                    //Toast.makeText(applicationContext, previousHash, Toast.LENGTH_SHORT).show()

                    loadingDialog.dismiss()
                    moveToMainPage()


                } else {
                    Log.d("BLOCK", "No Previous Hash")
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Unable to Load", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun fetch(view: View) {
        showLoadingDialog()
    }

    fun moveToMainPage(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        val intent2 = Intent(this, MyVotesActivity::class.java)
        startActivity(intent2)
        finishAffinity()

    }

    override fun onStateChange(bool: Boolean) {
        if(bool){
            Toast.makeText(applicationContext, "Verification Successful", Toast.LENGTH_SHORT).show()
            voteNow()
        }
        else{
            Toast.makeText(applicationContext, "Verification Unsuccessful", Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}