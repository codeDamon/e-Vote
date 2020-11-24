package com.example.e_votemvvm.Activities

import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isNotEmpty
import com.example.e_votemvvm.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import kotlin.time.minutes

class OtpVerificationActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var countTimeTv: TextView
    lateinit var verifyBtn: Button
    lateinit var voterIdTv : TextView
    lateinit var otpEnteredTv : OtpTextView
    lateinit var backBtn : ImageView

    lateinit var sharedPreferences: SharedPreferences
    private val SHARED_PREF = "MY_SHARED_PREF"

    private var voterId: String? = null
    private var phoneNo: String?=null

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var storedVerificationId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        countTimeTv = findViewById(R.id.count_time)
        verifyBtn = findViewById(R.id.verify_btn)
        voterIdTv = findViewById(R.id.voter_id_no)
        otpEnteredTv = findViewById(R.id.otp_view)
        backBtn = findViewById(R.id.back_icon)

        countTimeTv.setOnClickListener(this)
        verifyBtn.setOnClickListener(this)
        backBtn.setOnClickListener(this)

        //Firebase Auth
        auth = FirebaseAuth.getInstance()


        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)

        voterId = sharedPreferences.getString("voter_id",null)
        phoneNo = sharedPreferences.getString("phone_no",null)

        if(voterId==null)
            voterIdTv.text = "NULL"
        else
            voterIdTv.text = voterId


        callbacks  =  object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                Log.d("AUTH", "onCodeSent:$p0")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = p0
                //resendToken = token
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                Log.d("AUTH", "onVerificationCompleted:$p0")

                //Toast.makeText(applicationContext, "onVerificationCompleted", Toast.LENGTH_SHORT).show()

                signInWithPhoneAuthCredential(p0)

            }


            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("AUTH", "onVerificationFailed", e)

                //Toast.makeText(applicationContext, "Verification Failed", Toast.LENGTH_SHORT).show()


                if (e is FirebaseAuthInvalidCredentialsException) {

                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {

                    // The SMS quota for the project has been exceeded
                    // ...
                }

            }
        }

        if(voterId==null)
        {

        }
        else{
        phoneNo?.let {
           sendVerificationCode(it)
       }}

    }

    private fun sendVerificationCode(phoneNo : String ) {

        countFor60sec()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91"+phoneNo)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTH", "signInWithCredential:success")
                    //Toast.makeText(applicationContext, "signInWithCredential:success", Toast.LENGTH_SHORT).show()

                    intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()

                    // val user = task.result?.user
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("AUTH", "signInWithCredential:failure", task.exception)
                    //Toast.makeText(applicationContext, "signInWithCredential:failure", Toast.LENGTH_SHORT).show()

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    private fun countFor60sec(){
        val timer = object : CountDownTimer(59999,1000){
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished/1000

                if(sec<10)
                    countTimeTv.text = "00:0"+sec.toString()
                else
                    countTimeTv.text = "00:"+ sec.toString()
            }
            override fun onFinish() {
                countTimeTv.text = "Resend OTP"
            }
        }
        timer.start()
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id) {

                //for back button
                R.id.back_icon -> {
                    finish()
                }

                //for resend otp
                R.id.count_time ->{
                    if(countTimeTv.text == "Resend OTP"){
                        countFor60sec()

                        phoneNo?.let {
                            sendVerificationCode(it)
                        }

                    }
                }

                //for verify button
                R.id.verify_btn -> {

                    val enteredOtp = otpEnteredTv.otp
                    if(enteredOtp==""){
                        otpEnteredTv.showError()
                        Toast.makeText(applicationContext, "Enter OTP" , Toast.LENGTH_SHORT).show()

                    }
                    else if(enteredOtp!=null && enteredOtp.length < 6){
                        otpEnteredTv.showError()
                        Toast.makeText(applicationContext, "OTP must have 6 digits", Toast.LENGTH_SHORT).show()

                    }

                    else{
                        val enteredOtpNotNull : String = enteredOtp!!

                        val credential = PhoneAuthProvider.getCredential(storedVerificationId,enteredOtpNotNull)
                        signInWithPhoneAuthCredential(credential)

                    }



                }


            }
        }
    }

    fun nextPage(view: View) {
        intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}