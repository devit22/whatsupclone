package com.example.whatsupcloneinandorid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsupcloneinandorid.R
import com.example.whatsupcloneinandorid.adapters.ChatAdapter
import com.example.whatsupcloneinandorid.databinding.FragmentChatBinding
import com.example.whatsupcloneinandorid.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

private lateinit var binding :FragmentChatBinding
private lateinit var database: FirebaseDatabase
private lateinit var userlist:ArrayList<UserModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance();
        userlist = ArrayList()

        database.reference.child("users")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userlist.clear()
                    for(snapshot1 in snapshot.children){
                        val users = snapshot1.getValue(UserModel::class.java)
                        if(users!!.uid  != FirebaseAuth.getInstance().currentUser!!.uid){
                            userlist.add(users)
                        }

                    }
                    var adapter = ChatAdapter(context!!,userlist)

                    var layoutManager = LinearLayoutManager(context)
                    binding.userRecyclerview.layoutManager = layoutManager
                    binding.userRecyclerview.adapter = adapter


                }

                override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(context,"some error ",Toast.LENGTH_SHORT).show()
                }

            })

    }


}