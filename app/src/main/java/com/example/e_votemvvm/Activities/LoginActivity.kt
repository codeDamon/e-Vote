package com.example.e_votemvvm.Activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import com.google.gson.Gson
import org.json.JSONObject

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var voterIdEnteredEt : TextInputEditText
    lateinit var submitBtn : Button
    lateinit var helpIcon : ImageView
    lateinit var noVoterTv : TextView
    lateinit var loadingDialog : Dialog

    val SHARED_PREF = "MY_SHARED_PREF"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var myEdit: SharedPreferences.Editor





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        voterIdEnteredEt = findViewById(R.id.voter_id_entered)
        submitBtn = findViewById(R.id.submit_btn)
        helpIcon = findViewById(R.id.help_icon)
        noVoterTv =findViewById(R.id.no_voter_id)

        noVoterTv.setOnClickListener(this)
        submitBtn.setOnClickListener(this)
        helpIcon.setOnClickListener(this)


        sharedPreferences = getSharedPreferences(SHARED_PREF , MODE_PRIVATE)
        val newUser = sharedPreferences.getBoolean("newUser",true)


        //Not a new user -> open HomeActivity
        if(!newUser) {

            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    private fun fillListUsingFirebase():ArrayList<Post>{
        val list: ArrayList<Post> = ArrayList()

        val ref : DatabaseReference = FirebaseDatabase.getInstance().reference.child("posts")
        val check : Query = ref.orderByKey().limitToFirst(1)

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Log.d("POST", snapshot.value.toString())
                for (child in snapshot.children) {

                    //Log.d("POST", child.value.toString())



                    Log.d("POST", child.child("post_name").value.toString())
                    Log.d("POST", child.child("post_start").value.toString())
                    Log.d("POST", child.child("post_end").value.toString())
                    Log.d("POST", child.child("post_details").value.toString())
                    Log.d("POST", child.child("parties").child("AAP").value.toString())

                    val p_name = child.child("post_name").value.toString()
                    val p_start = child.child("post_start").value.toString()
                    val p_end = child.child("post_end").value.toString()
                    val p_details = child.child("post_details").value.toString()


                    val parties: ArrayList<Party> = ArrayList()

                    for(party in child.child("parties").children){
                        Log.d("POST",party.child("logo").value.toString())
                        parties.add(Party(party.child("name").value.toString(),
                            party.child("logo").value.toString(),
                            party.child("leader").value.toString(),
                            party.child("short_name").value.toString(),
                            false))
                    }

                    val gson = Gson();
                    val parties_str: String = gson.toJson(parties)

                    list.add(Post(p_name, p_start, p_end, parties_str,p_details))



                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("POST", "No Data")
            }

        })

        return list
    }

    fun showLoadingDialog() {
        loadingDialog = Dialog(this)
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        loadingDialog.setCancelable(true)

        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.show()
    }

    fun fetchVoterIdData(voterId: String){

        val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("voters")
        val check : Query = ref.orderByKey().equalTo(voterId)

        check.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    loadingDialog.dismiss()
                    var str: String = p0.value.toString()

                    val json: JSONObject = JSONObject(str)

                    val myEdit = sharedPreferences.edit()

                    myEdit.putString("voter_id",voterId)

                    str = json.getJSONObject(voterId).getString("phone_no")
                    myEdit.putString("phone_no", str )

                    str = json.getJSONObject(voterId).getString("name")
                    myEdit.putString("name", str )

                    str = json.getJSONObject(voterId).getString("gender")
                    myEdit.putString("gender", str )

                    str = json.getJSONObject(voterId).getString("city")
                    myEdit.putString("city", str )

                    str = json.getJSONObject(voterId).getString("dob")
                    myEdit.putString("dob", str )
                        .apply()



                    //Toast.makeText(applicationContext, str1, Toast.LENGTH_SHORT).show()

                   //TODO

                    val intent = Intent(applicationContext,OtpVerificationActivity::class.java)
                    startActivity(intent)



                } else {
                    Toast.makeText(applicationContext, "Invalid VoterID", Toast.LENGTH_SHORT).show()
                    loadingDialog.dismiss()
                    voterIdEnteredEt.error = "Invalid Voter ID"
                    voterIdEnteredEt.requestFocus()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Unable to Load", Toast.LENGTH_SHORT).show()

            }

        })

    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){

                //for help icon
                R.id.help_icon -> {

                    val eciHelpUrl = "https://ecisveep.nic.in/"
                    intent = Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(eciHelpUrl))
                    //Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show()
                    //intent = Intent(this,TestActivity2::class.java)
                    startActivity(intent)
                }

                //for no voter id
                R.id.no_voter_id -> {

                    val form6url: String = "https://nvsp.in/"
                    intent = Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(form6url))
                    //Toast.makeText(this, "No voter Id", Toast.LENGTH_SHORT).show()
                    //intent = Intent(this,OtpVerificationActivity::class.java)
                    startActivity(intent)

                }

                //for submit button
                R.id.submit_btn -> {

                    val enteredVoterId : String = voterIdEnteredEt.text.toString().trim()
                    if (enteredVoterId.isEmpty()) {
                        voterIdEnteredEt.error = "Please Enter Voter ID"
                        voterIdEnteredEt.requestFocus()
                    } else {
                        voterIdEnteredEt.error = null

                        showLoadingDialog()

                        fetchVoterIdData(enteredVoterId)


                    }


                }

            }
        }
    }

    fun nextPage(view: View) {
        intent = Intent(this,OtpVerificationActivity::class.java)
        startActivity(intent)
    }
}