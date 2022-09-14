package com.example.whatsupcloneinandorid.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsupcloneinandorid.R
import com.example.whatsupcloneinandorid.activiteis.ChatActivity
import com.example.whatsupcloneinandorid.databinding.ChatUserformateBinding
import com.example.whatsupcloneinandorid.models.UserModel

class ChatAdapter(var contextt:Context,var userlist:ArrayList<UserModel>):RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.MyViewHolder {
      return MyViewHolder(LayoutInflater.from(contextt).inflate(R.layout.chat_userformate,parent,false))
    }

    override fun onBindViewHolder(holder: ChatAdapter.MyViewHolder, position: Int) {
     var user = userlist[position]

        Glide.with(contextt).load(user.imageUrl).into(holder.binding.userImage)
        holder.binding.Username.text = user.name

        holder.itemview.setOnClickListener {
            var moveintent = Intent(contextt,ChatActivity::class.java)
            moveintent.putExtra("receiver",user.uid)
            moveintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            contextt.startActivity(moveintent)
        }

    }

    override fun getItemCount(): Int {
      return  userlist.size
    }

    class MyViewHolder(var itemview: View):RecyclerView.ViewHolder(itemview){
       var binding :ChatUserformateBinding = ChatUserformateBinding.bind(itemview)

    }
}