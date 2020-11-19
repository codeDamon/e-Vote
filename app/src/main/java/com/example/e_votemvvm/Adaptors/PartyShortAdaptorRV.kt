package com.example.e_votemvvm.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class PartyShortAdaptorRV(private val context: Context, private val list:ArrayList<Party>)
    : RecyclerView.Adapter<PartyShortAdaptorRV.PartyShortViewHolder>()  {


    inner class PartyShortViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val p_name_short = itemView.findViewById<TextView>(R.id.party_short)
        val p_logo = itemView.findViewById<CircleImageView>(R.id.party_logo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyShortViewHolder {
        val viewHolder = PartyShortViewHolder(LayoutInflater.from(context).inflate(R.layout.item_party_detail_rv, parent, false));
        return viewHolder
    }

    override fun onBindViewHolder(holder: PartyShortViewHolder, position: Int) {
        holder.p_name_short.text = list[position].partyShortName

        Glide.with(context)
            .load(list[position].partyLogo)
            .into(holder.p_logo)

    }

    override fun getItemCount(): Int {
       return list.size
    }
}