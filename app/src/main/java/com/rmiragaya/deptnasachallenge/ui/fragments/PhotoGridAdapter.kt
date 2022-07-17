package com.rmiragaya.deptnasachallenge.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rmiragaya.deptnasachallenge.R
import com.rmiragaya.deptnasachallenge.databinding.GridPhotoAdapterBinding
import com.rmiragaya.deptnasachallenge.models.DatePhotosItem
import com.rmiragaya.deptnasachallenge.utils.Constants

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
            binding.run {
                Glide.with(this.root)
                    .load("${Constants.IMAGE_URL}${photo.getOnlyDate()}/png/${photo.image}.png")
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