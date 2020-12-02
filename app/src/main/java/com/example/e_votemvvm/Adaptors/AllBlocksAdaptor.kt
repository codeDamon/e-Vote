package com.example.e_votemvvm.Adaptors

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Models.Block
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.R
import java.util.ArrayList

class AllBlocksAdaptor(private val context: Context,private val list:ArrayList<Block>, private val option: Int,
                       private val voterId:String, private val listener:InterfaceBlocksAdaptorRV) : RecyclerView.Adapter<AllBlocksAdaptor.AllBlocksViewHolder>() {


    interface InterfaceBlocksAdaptorRV{
        fun onItemClicked(position:Int, myVoteOrNot:Int)
    }

    inner class AllBlocksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){



        val b_hash = itemView.findViewById<TextView>(R.id.block_hash)
        val b_parent = itemView.findViewById<ConstraintLayout>(R.id.parent_block)
        val myVoteLogo = itemView.findViewById<ImageView>(R.id.my_vote_logo)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllBlocksViewHolder {
        val viewHolder = AllBlocksViewHolder(LayoutInflater.from(context).inflate(R.layout.item_block_rv, parent, false));

        viewHolder.b_parent.setOnClickListener{
            listener.onItemClicked(viewHolder.adapterPosition,viewHolder.myVoteLogo.visibility)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: AllBlocksViewHolder, position: Int) {


        if(list[position].voter_id == voterId)
            holder.myVoteLogo.visibility = View.VISIBLE


        holder.b_hash.text = list[position].hash
        //holder.b_parent.setBackgroundColor(Color.parseColor(R.color.colorAccent.toString()))

    }

    override fun getItemCount(): Int {
        return list.size
    }
}