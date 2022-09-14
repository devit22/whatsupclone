package com.example.whatsupcloneinandorid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.whatsupcloneinandorid.activiteis.NumberActivity
import com.example.whatsupcloneinandorid.activiteis.ProfileActivity
import com.example.whatsupcloneinandorid.adapters.ViewPagerAdapter
import com.example.whatsupcloneinandorid.databinding.ActivityMainBinding
import com.example.whatsupcloneinandorid.ui.CallFragment
import com.example.whatsupcloneinandorid.ui.ChatFragment
import com.example.whatsupcloneinandorid.ui.StatusFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val fragmentarraylist = ArrayList<Fragment>()

        fragmentarraylist.add(ChatFragment())
        fragmentarraylist.add(StatusFragment())
        fragmentarraylist.add(CallFragment())
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            startActivity(Intent(this,NumberActivity::class.java))
            finish()
        }


        val viewPagerAdapter = ViewPagerAdapter(this,supportFragmentManager,fragmentarraylist)

        binding!!.viewpager.adapter = viewPagerAdapter
            binding!!.tabs.setupWithViewPager(binding!!.viewpager)



    }

}