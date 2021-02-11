package com.example.unsplashphotoloader

import android.content.Context
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_collection.view.*
import kotlinx.android.synthetic.main.progress_loading.view.*



class CollectionsAdapter(val context: Context): RecyclerView.Adapter<CollectionsAdapter.ViewHolder>(){

    var mCollection: ArrayList<Collection> = ArrayList()

    fun setCollection(newCollection: ArrayList<Collection>){
        mCollection.clear()
        mCollection.addAll(newCollection)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsAdapter.ViewHolder {
        val context = parent.context
        return ViewHolder(itemView = LayoutInflater.from(context).inflate(R.layout.item_collection,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(collection = mCollection[position])
    }

    override fun getItemCount(): Int {
        return mCollection.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{

        var collectionTextView: TextView = itemView.findViewById(R.id.collection_title)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(collection: Collection){
            collectionTextView.text = collection.title
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val collectionId = mCollection[position].id
                val intent = Intent(context, CollectionPhotoActivity::class.java)
                intent.putExtra("id", collectionId)
                val bundle: Bundle = Bundle()
                startActivity(context,intent,bundle)
            }
        }
    }



}