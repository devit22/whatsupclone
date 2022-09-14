package com.example.whatsupcloneinandorid.activiteis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.whatsupcloneinandorid.MainActivity
import com.example.whatsupcloneinandorid.R
import com.example.whatsupcloneinandorid.databinding.ActivityNumberBinding
import com.example.whatsupcloneinandorid.databinding.ActivityOtpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
private  lateinit var binding: ActivityOtpBinding
    private lateinit var myresendtoken: PhoneAuthProvider.ForceResendingToken
    private  var verification_code: String="somecode"
    private lateinit var auth :FirebaseAuth
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Pleas wait...")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog = builder.create()

         dialog.show()
        if(intent != null)
        {
            val text = intent.getStringExtra("number")
            sendotp(text!!)
        }else{
            Toast.makeText(applicationContext,"Number is not verified",Toast.LENGTH_SHORT).show()
        }

binding.submitOtpBtn.setOnClickListener {
    var otptext = binding.otpEdit.text.toString()

    if(TextUtils.isEmpty(otptext)){
        Toast.makeText(applicationContext,"Please enter the otp",Toast.LENGTH_SHORT).show()
    }else{
        verficode(otptext)
    }
}
    }

    private fun sendotp(mynumber:String) {

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(mynumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this@OtpActivity) // Activity (for callback binding)
                .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

    }
    val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                binding.otpEdit.setText(code!!.toString())
                Thread.sleep(1000)
                verficode(code)
            }

            override fun onVerificationFailed(e: FirebaseException) {
dialog.dismiss()
                Toast.makeText(applicationContext, "error " + e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                dialog.dismiss()
                verification_code = s
                myresendtoken=forceResendingToken
            }
        }

    fun verficode(code: String) {
        dialog.show()
        val credential = PhoneAuthProvider.getCredential(verification_code, code)
        singinwithcredention(credential)
    }

    private fun singinwithcredention(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, OnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this@OtpActivity, ProfileActivity::class.java)
                startActivity(intent)
                finish()
                dialog.dismiss()
                Toast.makeText(applicationContext,"Number verified", Toast.LENGTH_SHORT).show()
            }else{
                dialog.dismiss()
                Toast.makeText(applicationContext,"error -> ${it.exception}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}