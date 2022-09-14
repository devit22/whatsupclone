package com.example.whatsupcloneinandorid.activiteis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.whatsupcloneinandorid.MainActivity
import com.example.whatsupcloneinandorid.R
import com.example.whatsupcloneinandorid.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

class NumberActivity : AppCompatActivity() {
   private lateinit var binding : ActivityNumberBinding
   private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        binding.submitNumberBtn.setOnClickListener {
            var numbercode = binding.countrypicker.selectedCountryCodeWithPlus

            var numbertext = binding.numberEdit.text.toString()

            if(TextUtils.isEmpty(numbertext)){
                Toast.makeText(applicationContext,"Please enter number first",Toast.LENGTH_LONG).show()
            }else{
                val finalnumber =numbercode+numbertext

                val numberintent = Intent(this,OtpActivity::class.java)
                numberintent.putExtra("number",finalnumber)
                startActivity(numberintent)
            }
        }
    }
}