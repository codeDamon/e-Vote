package com.example.e_votemvvm.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.e_votemvvm.Models.Post
import com.example.e_votemvvm.R
import java.util.*

class PostAdaptorRV(private val context: Context, private val listener: InterfacePostAdaptorRV)
    : RecyclerView.Adapter<PostAdaptorRV.PostViewHolder>() {

    private val list = ArrayList<Post>()



    interface InterfacePostAdaptorRV{
        fun onItemClicked(post: Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val viewHolder = PostViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post_rv, parent, false));


        viewHolder.parent_lay.setOnClickListener{
            listener.onItemClicked(list[viewHolder.adapterPosition])
        }

        return viewHolder;
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        var tempVar: String = list[position].postName

        holder.p_initial.text = "" + tempVar[0]
        holder.p_name.text = tempVar

        tempVar = list[position].postStart
        holder.p_start.text = tempVar

        tempVar = list[position].postEnd
        holder.p_end.text = tempVar




    }

    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val p_name = itemView.findViewById<TextView?>(R.id.post_name)
        val p_initial = itemView.findViewById<TextView?>(R.id.post_initials)
        val p_start = itemView.findViewById<TextView?>(R.id.post_start)
        val p_end = itemView.findViewById<TextView?>(R.id.post_end)
        val parent_lay = itemView.findViewById<ConstraintLayout>(R.id.parent_layout)


    }

    fun updateList(newList: List<Post>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}