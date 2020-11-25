package com.example.e_votemvvm.Utilities

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class BiometricVerification(val context: Context,private val listener: OnVerificationStateChangeListener) {

    private val TAG = "BIO_AUTH"
    private lateinit var biometricPrompt: BiometricPrompt
    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)

                Log.d(TAG, "onAuthenticationError: $errString")
                listener.onStateChange(false)

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)

                Log.d(TAG, "onAuthenticationSucceeded")
                listener.onStateChange(true)


            }
        }

    interface OnVerificationStateChangeListener{
        fun onStateChange(bool:Boolean);
    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun buildBiometricPrompt(title: String, subtitle: String, negative:String) {

        val executor = ContextCompat.getMainExecutor(context)


        @RequiresApi(Build.VERSION_CODES.P)
        biometricPrompt = BiometricPrompt.Builder(context)
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButton(
                negative,
                context.mainExecutor,
                DialogInterface.OnClickListener { dialog, which ->

                    Log.d(TAG, negative)
                    listener.onStateChange(false)


                }).build()

        biometricPrompt.authenticate(getCancellationSignal(), executor, authenticationCallback)



    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            Log.d(TAG,"Cancelled by user" )
            listener.onStateChange(false)

        }
        return cancellationSignal as CancellationSignal
    }

    fun checkBiometricSupport(): Boolean {
        val keypadManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keypadManager.isDeviceSecure) {
            Toast.makeText(context, "Biometric authentication disabled", Toast.LENGTH_SHORT)
                .show()

            Log.d(TAG,"Biometric authentication disabled" )
            listener.onStateChange(false)

            return false
        } else if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                context,
                "Biometric authentication permission disabled",
                Toast.LENGTH_SHORT
            ).show()

            Log.d(TAG,"Biometric authentication permission disabled" )
            listener.onStateChange(false)

            return false
        } else
        {
            if(context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
                return true
            }
            else{
                listener.onStateChange(false)
                return false


            }
        }
    }

}