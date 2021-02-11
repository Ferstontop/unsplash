package com.example.unsplashphotoloader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startForegroundService
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo.view.*
import kotlinx.android.synthetic.main.item_image.view.*

class PhotoAdapter(val context: Context): RecyclerView.Adapter<PhotoAdapter.ViewHolder>(){

    var mPhotos: ArrayList<Photo> = ArrayList()

    fun setPhoto(newPhoto: ArrayList<Photo>){
        mPhotos.clear()
        mPhotos.addAll(newPhoto)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.ViewHolder {
        val context = parent.context
        return ViewHolder(itemView = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photo = mPhotos[position])
    }

    override fun getItemCount(): Int {
        return mPhotos.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{

        var photoImgView: ImageView = itemView.findViewById(R.id.photo)


        init {
            itemView.setOnClickListener(this)
        }

        fun bind(photo: Photo){

            Picasso.get()
                .load(photo.imageUrl)
                .fit()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .tag(context)
                .into(photoImgView)

        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val photo = mPhotos[position]
                val intent = Intent(context, PhotoActivity::class.java)
                intent.putExtra(PhotoActivity.EXTRA_PHOTO, photo)
                val bundle: Bundle = Bundle()
                startActivity(context,intent,bundle)
            }
        }


    }



}