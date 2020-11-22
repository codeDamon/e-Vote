package com.example.e_votemvvm.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.e_votemvvm.R
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var voterIdEnteredTv : TextInputEditText
    lateinit var submitBtn : Button
    lateinit var helpIcon : ImageView
    lateinit var noVoterTv : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        voterIdEnteredTv = findViewById(R.id.voter_id_entered)
        submitBtn = findViewById(R.id.submit_btn)
        helpIcon = findViewById(R.id.help_icon)
        noVoterTv =findViewById(R.id.no_voter_id)

        noVoterTv.setOnClickListener(this)
        submitBtn.setOnClickListener(this)
        helpIcon.setOnClickListener(this)





    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){

                //for help icon
                R.id.help_icon -> {
                    Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show()
                }

                //for no voter id
                R.id.no_voter_id -> {
                    Toast.makeText(this,"No voter Id",Toast.LENGTH_SHORT).show()

                }

                //for submit button
                R.id.submit_btn -> {



                }

            }
        }
    }
}