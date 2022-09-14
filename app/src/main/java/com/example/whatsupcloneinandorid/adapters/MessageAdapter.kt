package com.example.whatsupcloneinandorid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsupcloneinandorid.R
import com.example.whatsupcloneinandorid.databinding.ReceiverItemLayoutBinding
import com.example.whatsupcloneinandorid.databinding.SentItemLayoutBinding
import com.example.whatsupcloneinandorid.models.MessageModel
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private var context:Context,private var messagelist:ArrayList<MessageModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var ITEM_SENT =1
        var RECEVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     return  if(viewType == ITEM_SENT) SendViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false))
        else ReceiveViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_item_layout,parent,false))
    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().currentUser!!.uid  == messagelist[position].senderId) ITEM_SENT else RECEVE
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      var message = messagelist[position]
        if(holder.itemViewType == ITEM_SENT){
            val viewholder =    holder as SendViewHolder
             viewholder.binding.usermessage.text = message.message
        }else{
            val viewholder = holder as ReceiveViewHolder
            viewholder.binding.usermessage.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }

  inner  class SendViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
      var binding = SentItemLayoutBinding.bind(itemView)
  }

    inner  class ReceiveViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
          var binding = ReceiverItemLayoutBinding.bind(itemView)
    }


}