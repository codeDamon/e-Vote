package com.example.e_votemvvm.Activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Adaptors.PostAdaptorRV
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.R
import com.example.e_votemvvm.Utilities.BiometricVerification
import com.example.e_votemvvm.ViewModels.PostViewModel
import com.google.firebase.database.*
import com.google.gson.Gson


class MainActivity : AppCompatActivity() , PostAdaptorRV.InterfacePostAdaptorRV , View.OnClickListener,
    BiometricVerification.OnVerificationStateChangeListener{


    val SHARED_PREF = "MY_SHARED_PREF"
    lateinit var sharedPreferences: SharedPreferences

    lateinit var viewModel: PostViewModel
    lateinit var loadingDialog : Dialog
    lateinit var locTv: TextView



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_posts)
        locTv = findViewById(R.id.location)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val adapter = PostAdaptorRV(this, this)

        /*val profileLogo = findViewById<ImageView>(R.id.profile_logo)
        profileLogo.setOnClickListener(this)*/

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application
            )
        )
                .get(PostViewModel::class.java)


        viewModel.allPosts.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }

        })


        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        val newUser = sharedPreferences.getBoolean("newUser", true)
        val locCity =  sharedPreferences.getString("city", "Delhi")
        locTv.text = locCity

        val myEdit = sharedPreferences.edit()

        if(newUser) {
            myEdit.putBoolean("newUser", false).apply()
            fillListUsingFirebase()
        }
        else{
            if(sharedPreferences.getBoolean("isBioVerificationEnabled", false)){
                val biometricVerification = BiometricVerification(this, this)
                if(biometricVerification.checkBiometricSupport()){
                    biometricVerification.buildBiometricPrompt(
                        "Biometric Verification",
                        "Please Verify",
                        "Close App"
                    )
                }
            }
            }
        }

    fun showLoadingDialog() {
        loadingDialog = Dialog(this)
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        loadingDialog.setCancelable(true)

        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.show()
    }

    private fun fillListUsingFirebase(){

        showLoadingDialog()

        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("posts")
        val check: Query = ref.orderByKey()

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Log.d("POST", snapshot.value.toString())
                for (child in snapshot.children) {

                    //Log.d("POST", child.value.toString())

                    val p_name = child.child("post_name").value.toString()
                    val p_start = child.child("post_start").value.toString()
                    val p_end = child.child("post_end").value.toString()
                    val p_details = child.child("post_details").value.toString()


                    val parties: ArrayList<Party> = ArrayList()

                    for (party in child.child("parties").children) {
                        Log.d("POST", party.child("logo").value.toString())
                        parties.add(
                            Party(
                                party.child("name").value.toString(),
                                party.child("logo").value.toString(),
                                party.child("leader").value.toString(),
                                party.child("short_name").value.toString(),
                                false
                            )
                        )
                    }

                    val gson = Gson();
                    val parties_str: String = gson.toJson(parties)

                    viewModel.insertPost(Post(p_name, p_start, p_end, parties_str, p_details))


                }
                loadingDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("POST", "No Data")
            }

        })
    }

    /*private fun fillList(){


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
    }*/

    override fun onItemClicked(post: Post) {
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("post", post)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
           /* R.id.profile_logo -> {
                val intent = Intent(this,TestActivity::class.java)
                startActivity(intent)
            }*/
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.my_vote_menu -> {
                intent = Intent(this,MyVotesActivity::class.java)
                startActivity(intent)

            }
            R.id.settings_menu -> {
                intent = Intent(this,SettingActivity::class.java)
                startActivity(intent)

            }
            R.id.logout_menu -> {
                Toast.makeText(applicationContext, "Logout Successful", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().clear().apply()
                viewModel.deleteAllPosts()
                intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStateChange(bool: Boolean) {
        if(bool){
            Toast.makeText(applicationContext, "Verification Successful", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(applicationContext, "Verification Unsuccessful", Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}