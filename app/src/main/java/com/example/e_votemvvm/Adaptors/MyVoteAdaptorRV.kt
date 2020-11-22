package com.example.e_votemvvm.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Models.Vote
import com.example.e_votemvvm.R
import java.util.ArrayList

class MyVoteAdaptorRV(private val context: Context) : RecyclerView.Adapter<MyVoteAdaptorRV.MyVoteViewHolder>() {

    private val list = ArrayList<Vote>()

    inner class MyVoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val p_name = itemView.findViewById<TextView?>(R.id.post_name)
        val party = itemView.findViewById<TextView?>(R.id.party_name)
        val dateTime = itemView.findViewById<TextView?>(R.id.date_time)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVoteViewHolder {
        val viewHolder = MyVoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_vote_rv, parent, false));
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyVoteViewHolder, position: Int) {
        holder.p_name.text = list[position].votePost
        holder.party.text = list[position].voteParty
        holder.dateTime.text = list[position].time
    }

    fun updateList(newList: List<Vote>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return list.size
    }
}