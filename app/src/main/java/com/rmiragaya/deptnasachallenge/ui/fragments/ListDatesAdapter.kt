package com.rmiragaya.deptnasachallenge.ui.fragments

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rmiragaya.deptnasachallenge.databinding.DateListAdapterBinding
import com.rmiragaya.deptnasachallenge.databinding.DateListAdapterBinding.inflate
import com.rmiragaya.deptnasachallenge.models.DateResponse
import com.rmiragaya.deptnasachallenge.models.DateResponseItem
import com.rmiragaya.deptnasachallenge.models.DownloadState

class ListDatesAdapter(val onClick : (DateResponse?) -> Unit) : RecyclerView.Adapter<ListDatesAdapter.DateViewHolder>() {


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<DateResponseItem>(){
        override fun areItemsTheSame(oldItem: DateResponseItem, newItem: DateResponseItem): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DateResponseItem, newItem: DateResponseItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        differ.currentList[position].let { article -> holder.bind(article) }
    }


    inner class DateViewHolder(private val binding: DateListAdapterBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(article: DateResponseItem){
            article.date?.let { binding.date.text = it }
            setDownloadStatus(article.downloadState ?: DownloadState.IDLE, binding)
            setArticle(article)
        }

        private fun setDownloadStatus(downloadState: DownloadState, binding: DateListAdapterBinding) {
            with(binding){
                when(downloadState){
                    DownloadState.IDLE -> {
                        idle.visibility = View.VISIBLE
                        downloading.visibility = View.INVISIBLE
                        downloaded.visibility = View.INVISIBLE

                    }
                    DownloadState.LOADING -> {
                        idle.visibility = View.INVISIBLE
                        downloading.visibility = View.VISIBLE
                        downloaded.visibility = View.INVISIBLE

                    }
                    DownloadState.SUCCES -> {
                        idle.visibility = View.INVISIBLE
                        downloading.visibility = View.INVISIBLE
                        downloaded.visibility = View.VISIBLE
                    }
                }
            }

        }

        private fun setArticle (item: DateResponseItem?){
            itemView.setOnClickListener {
                onClick(item?.datePhotos)
            }
        }
    }


}