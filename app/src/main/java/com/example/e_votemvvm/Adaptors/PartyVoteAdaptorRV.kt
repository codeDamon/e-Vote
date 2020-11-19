package com.example.e_votemvvm.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class PartyVoteAdaptorRV(private val context: Context, private val list: ArrayList<Party>, private val listener: InterfacePartyAdaptorRV)
    : RecyclerView.Adapter<PartyVoteAdaptorRV.PartyVoteViewHolder>() {

    var partySelected : Int = -1

    interface InterfacePartyAdaptorRV{
        fun onItemClicked(party:Party,position: Int)
    }

    inner class PartyVoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val p_name = itemView.findViewById<TextView?>(R.id.party_name)
        val p_logo = itemView.findViewById<CircleImageView>(R.id.party_logo)
        val p_leader = itemView.findViewById<TextView?>(R.id.party_leader)
        val p_select = itemView.findViewById<ImageView>(R.id.select_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyVoteViewHolder {
        val viewHolder = PartyVoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_party_vote_rv, parent, false));



        viewHolder.p_select.setOnClickListener{

            if(partySelected == -1){
                partySelected = viewHolder.adapterPosition
                viewHolder.p_select.setColorFilter(ContextCompat.getColor(context,R.color.green),android.graphics.PorterDuff.Mode.SRC_IN)
                listener.onItemClicked(list[viewHolder.adapterPosition],viewHolder.adapterPosition)
            }
            else{
                if(viewHolder.adapterPosition == partySelected){
                    partySelected = -1
                    listener.onItemClicked(list[viewHolder.adapterPosition],partySelected)
                    viewHolder.p_select.setColorFilter(ContextCompat.getColor(context,R.color.red),android.graphics.PorterDuff.Mode.SRC_IN)
                }
                else{
                    Toast.makeText(context, "Un-select the previous selected party", Toast.LENGTH_SHORT).show()
                }
            }

        }

        return viewHolder;
    }

    override fun onBindViewHolder(holder: PartyVoteViewHolder, position: Int) {
        holder.p_name.text = list[position].partyName +"("+list[position].partyShortName +")"
        holder.p_leader.text = list[position].leader

        Glide.with(context)
            .load(list[position].partyLogo)
            .into(holder.p_logo)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}