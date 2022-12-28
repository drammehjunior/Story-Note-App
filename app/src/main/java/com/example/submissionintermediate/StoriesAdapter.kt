package com.example.submissionintermediate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionintermediate.pojo.ListStoryItem

class StoriesAdapter(private val listStories: List<ListStoryItem>): RecyclerView.Adapter<StoriesAdapter.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.story_row, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            userName.text = listStories[position].name.toString()
            Glide.with(holder.itemView.context)
                .load(listStories[position].photoUrl)
                .into(imgUser)

            itemView.setOnClickListener{ onItemClickCallback.onItemClicked(listStories[position])}
        }
    }

    override fun getItemCount() = listStories.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgUser: ImageView = itemView.findViewById(R.id.img_user_picture)
        var userName: TextView = itemView.findViewById(R.id.txt_username)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}