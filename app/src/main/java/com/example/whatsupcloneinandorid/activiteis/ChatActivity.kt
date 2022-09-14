package com.example.whatsupcloneinandorid.activiteis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.whatsupcloneinandorid.R
import com.example.whatsupcloneinandorid.adapters.MessageAdapter
import com.example.whatsupcloneinandorid.databinding.ActivityChatBinding
import com.example.whatsupcloneinandorid.models.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var senderUid:String
    private lateinit var receriverUid:String
    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    private lateinit var auth:FirebaseAuth
    private lateinit var list:ArrayList<MessageModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        list = ArrayList()
        senderUid = auth.currentUser!!.uid
        if(intent != null){
            receriverUid = intent.getStringExtra("receiver").toString()
        }
        senderRoom = senderUid+receriverUid
        receiverRoom = receriverUid+senderUid

        binding.imageView.setOnClickListener {
            val messagetext = binding.messageBox.text.toString()
            if(TextUtils.isEmpty(messagetext)){
                Toast.makeText(applicationContext,"please entetr the message",Toast.LENGTH_SHORT).show()
            }else{
                  val message = MessageModel(messagetext,senderUid, Date().time)

                val randomkey = database.reference.push().key
                database.reference.child("chats").child(senderRoom).child("message").child(randomkey!!)
                    .setValue(message).addOnSuccessListener {
                        database.reference.child("chats").child(receiverRoom).child("message").child(randomkey)
                            .setValue(message).addOnSuccessListener {
                                binding.messageBox.text = null
                                Toast.makeText(applicationContext,"Message Sent",Toast.LENGTH_SHORT).show()
                            }

                    }
            }
        }

        database.reference.child("chats").child(senderRoom).child("message").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                        list.clear()

                for(snapshot1 in snapshot.children){
                    val data =  snapshot1.getValue(MessageModel::class.java)
                    list.add(data!!)

                }
                binding.messagerecyclerView.adapter = MessageAdapter(applicationContext,list)
            }

            override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(applicationContext,"error => ${error.message}",Toast.LENGTH_SHORT).show()
            }

        })
    }
}