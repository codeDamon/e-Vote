package com.example.e_votemvvm.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.e_votemvvm.R
import com.example.e_votemvvm.Utilities.EncryptionAES

class TestActivity2 : AppCompatActivity() ,View.OnClickListener{

    lateinit var ptext: EditText
    lateinit var ctext: EditText
    lateinit var keyEt: EditText
    lateinit var pcbtn : Button
    lateinit var cpbtn: Button

    lateinit var aes: EncryptionAES
    lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        ptext = findViewById(R.id.plain_text)
        ctext  = findViewById(R.id.cypher)
        pcbtn = findViewById(R.id.ptoc)
        cpbtn = findViewById(R.id.ctop)
        keyEt = findViewById(R.id.key)

        pcbtn.setOnClickListener(this)
        cpbtn.setOnClickListener(this)

        aes = EncryptionAES()
        key = "appleisredisredr"





    }

    fun encrypt() {

        key = keyEt.text.toString()
        ctext.setText( aes.encryptAes(ptext.text.toString(),key).toString())




    }
    fun decrypt() {

        key = keyEt.text.toString()

        var str = ctext.text.toString()
        if(str!=null)

        str =  aes.decryptAes(str,key)

        ptext.setText(str)


    }

    override fun onClick(v: View?) {

        if(v!=null){
            when(v.id) {

                R.id.ptoc ->{

                    encrypt()

                }
                R.id.ctop ->{

                    decrypt()

                }
            }
        }

    }
}