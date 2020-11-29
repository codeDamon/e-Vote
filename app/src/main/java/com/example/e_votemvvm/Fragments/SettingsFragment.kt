package com.example.e_votemvvm.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.e_votemvvm.R
import com.example.e_votemvvm.Utilities.BiometricVerification

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener,
    BiometricVerification.OnVerificationStateChangeListener {

    var sharedPreferences: SharedPreferences? = null
    private val SHARED_PREF = "MY_SHARED_PREF"
    var previousStatusOfBio: Boolean? = null
    lateinit var toast: Toast

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_layout, rootKey)

        toast = Toast.makeText(context, "Biometric Verification turned-off", Toast.LENGTH_SHORT)


        val pref = findPreference<SwitchPreference>("isBioVerificationEnabled")
        if (pref != null) {
            pref.onPreferenceChangeListener = this
        }

        sharedPreferences = context?.getSharedPreferences(
            SHARED_PREF,
            Context.MODE_PRIVATE
        )

        previousStatusOfBio = sharedPreferences?.getBoolean("isBioVerificationEnabled", false)

        pref?.isChecked = false
        pref?.isChecked = previousStatusOfBio == true




    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        if (preference is SwitchPreference) {
            if (preference.isChecked) {

                toast.cancel()
                toast = Toast.makeText(context, "Biometric Verification turned-off", Toast.LENGTH_SHORT)
                    toast.show()
                sharedPreferences?.edit()?.putBoolean("isBioVerificationEnabled", false)?.apply()

                } else {

                val biometricVerification = context?.let { BiometricVerification(it, this) }
                if (biometricVerification!!.checkBiometricSupport()) {
                    biometricVerification.buildBiometricPrompt(
                        "Biometric Verification",
                        "Please Verify",
                        "Cancel"
                    )



                }

            }

        }
        return true
    }

    override fun onStateChange(bool: Boolean) {
        if(bool){

            toast.cancel()
            toast= Toast.makeText(context, "Biometric Verification turned-on", Toast.LENGTH_SHORT)
                toast.show()

            sharedPreferences?.edit()?.putBoolean("isBioVerificationEnabled", true)
                ?.apply()


        }
        else{
            toast.cancel()
            toast=  Toast.makeText(context, "Biometric Verification Failed", Toast.LENGTH_SHORT)
                toast.show()

            sharedPreferences?.edit()?.putBoolean("isBioVerificationEnabled", false)?.apply()


        }
    }
}
