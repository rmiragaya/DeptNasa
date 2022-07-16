package com.rmiragaya.deptnasachallenge.ui.fragments

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rmiragaya.deptnasachallenge.R
import com.rmiragaya.deptnasachallenge.databinding.GridPhotoAdapterBinding
import com.rmiragaya.deptnasachallenge.models.DatePhotosItem

class PhotoGridAdapter (val onClick : (DatePhotosItem) -> Unit) :  RecyclerView.Adapter<PhotoGridAdapter.PhotoViewHolder>()  {



    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<DatePhotosItem>() {
        override fun areItemsTheSame(oldItem: DatePhotosItem, newItem: DatePhotosItem): Boolean {
            return oldItem.identifier == newItem.identifier
        }

        override fun areContentsTheSame(oldItem: DatePhotosItem, newItem: DatePhotosItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            GridPhotoAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        differ.currentList[position].let { photoItem -> holder.bind(photoItem) }
    }

    inner class PhotoViewHolder(private val binding: GridPhotoAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: DatePhotosItem) {

            val dateFormat = photo.date?.replace("-", "/")

            binding.run {
                Glide.with(this.root)
                    .load("https://epic.gsfc.nasa.gov/archive/enhanced/${dateFormat?.split(" ")?.first()}/png/${photo.image}.png")
                    .thumbnail(0.25f)
                    .transform(CenterCrop(), RoundedCorners(25))
                    .placeholder(R.drawable.ic_placeholder)
                    .into(binding.photo)
            }

            setPhoto(photo)
        }

        private fun setPhoto (photo: DatePhotosItem){
            itemView.setOnClickListener {
                onClick(photo)
            }
        }
    }
}