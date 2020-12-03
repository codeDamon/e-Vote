package com.example.e_votemvvm.Adaptors

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Models.Block
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.R
import com.example.e_votemvvm.Utilities.EncryptionAES
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.wajahatkarim3.easyflipview.EasyFlipView
import org.json.JSONObject
import java.util.ArrayList

class AllBlocksAdaptor(private val context: Context,private val list:ArrayList<Block>, private val option: Int,
                       private val voterId:String, private val listener:InterfaceBlocksAdaptorRV,
                       private val key:String) : RecyclerView.Adapter<AllBlocksAdaptor.AllBlocksViewHolder>() {



    interface InterfaceBlocksAdaptorRV{
        fun onItemClicked(position:Int, myVoteOrNot:Int)
    }

    inner class AllBlocksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){



        //flip-view(parent)
        val b_parent = itemView.findViewById<EasyFlipView>(R.id.parent_block)

        //front-view
        val b_hash = itemView.findViewById<TextView>(R.id.block_hash)
        val lockLogo = itemView.findViewById<ImageView>(R.id.front_lock)


        //back-view
        val cardView = itemView.findViewById<CardView>(R.id.card)
        val unlockLogo = itemView.findViewById<ImageView>(R.id.unlock_logo)
        val lockedLogo = itemView.findViewById<ImageView>(R.id.locked_logo)
        val tv = itemView.findViewById<TextView>(R.id.tv_13)


        val postName = itemView.findViewById<TextView>(R.id.post_name)
        val partyName = itemView.findViewById<TextView>(R.id.party_name)
        val date = itemView.findViewById<TextView>(R.id.time)




    }

    private fun setDataToFrontView(encryptedData:String, viewHolder: AllBlocksViewHolder){

        val encryptionAES = EncryptionAES()
        val decryptedData = encryptionAES.decryptAes(encryptedData,key)

        //val data = "{voter:\"ASH\"}"


        val jsonObject :JSONObject = JSONObject(decryptedData)

        viewHolder.postName.text = jsonObject.getString("postVoted")
        viewHolder.partyName.text = jsonObject.getString("partyVoted")
        viewHolder.date.text = jsonObject.getString("time")


        //Toast.makeText(context,jsonObject.getString("voterId")+key,Toast.LENGTH_SHORT).show()
         //Toast.makeText(context,decryptedData,Toast.LENGTH_SHORT).show()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllBlocksViewHolder {
        val viewHolder = AllBlocksViewHolder(LayoutInflater.from(context).inflate(R.layout.item_block_rv, parent, false));

        viewHolder.b_parent.onFlipListener = object : EasyFlipView.OnFlipAnimationListener{
            override fun onViewFlipCompleted(
                easyFlipView: EasyFlipView?,
                newCurrentSide: EasyFlipView.FlipState?
            ) {
                if(newCurrentSide == EasyFlipView.FlipState.BACK_SIDE){
                    if(list[viewHolder.adapterPosition].voter_id != voterId){
                        viewHolder.cardView.visibility = View.GONE
                        viewHolder.unlockLogo.visibility = View.GONE
                        viewHolder.lockedLogo.visibility = View.VISIBLE
                        viewHolder.tv.visibility = View.VISIBLE
                    }
                    else{
                        setDataToFrontView(list[viewHolder.adapterPosition].encryptedData, viewHolder)

                        viewHolder.cardView.visibility = View.VISIBLE
                        viewHolder.unlockLogo.visibility = View.VISIBLE
                        viewHolder.lockedLogo.visibility = View.GONE
                        viewHolder.tv.visibility = View.GONE
                    }
                }

            }

        }


        return viewHolder
    }

    override fun onBindViewHolder(holder: AllBlocksViewHolder, position: Int) {


        if(list[position].voter_id == voterId)
            holder.lockLogo.visibility = View.VISIBLE


        holder.b_hash.text = list[position].hash

    }

    override fun getItemCount(): Int {
        return list.size
    }
}