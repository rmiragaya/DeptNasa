package com.rmiragaya.deptnasachallenge.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rmiragaya.deptnasachallenge.databinding.DateListAdapterBinding
import com.rmiragaya.deptnasachallenge.databinding.DateListAdapterBinding.inflate
import com.rmiragaya.deptnasachallenge.models.DatePhotosItem
import com.rmiragaya.deptnasachallenge.models.DateResponseItem
import com.rmiragaya.deptnasachallenge.models.DownloadState
import com.rmiragaya.deptnasachallenge.models.PhotoList

class DatesListAdapter(val onClick: (PhotoList) -> Unit) :
    RecyclerView.Adapter<DatesListAdapter.DateViewHolder>() {

    private var data = emptyList<DateResponseItem>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        data[position].let { article -> holder.bind(article) }
    }

    inner class DateViewHolder(private val binding: DateListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dateItem: DateResponseItem) {
            dateItem.date.let { binding.date.text = it }
            setDownloadStatus(dateItem.downloadState ?: DownloadState.IDLE, binding)
            setArticle(dateItem.datePhotos)
        }

        private fun setDownloadStatus(
            downloadState: DownloadState,
            binding: DateListAdapterBinding
        ) {
            with(binding) {
                when (downloadState) {
                    DownloadState.IDLE -> {
                        idle.visibility = View.VISIBLE
                        downloading.visibility = View.INVISIBLE
                        downloaded.visibility = View.INVISIBLE
                        error.visibility = View.INVISIBLE
                    }
                    DownloadState.LOADING -> {
                        idle.visibility = View.INVISIBLE
                        downloading.visibility = View.VISIBLE
                        downloaded.visibility = View.INVISIBLE
                        error.visibility = View.INVISIBLE
                    }
                    DownloadState.SUCCES -> {
                        idle.visibility = View.INVISIBLE
                        downloading.visibility = View.INVISIBLE
                        downloaded.visibility = View.VISIBLE
                        error.visibility = View.INVISIBLE
                    }
                    DownloadState.ERROR -> {
                        idle.visibility = View.INVISIBLE
                        downloading.visibility = View.INVISIBLE
                        downloaded.visibility = View.INVISIBLE
                        error.visibility = View.VISIBLE
                    }
                }
            }
        }

        private fun setArticle(item: ArrayList<DatePhotosItem>?) {
            itemView.setOnClickListener {
                onClick(PhotoList(item))
            }
        }
    }

    fun setData(newList: MutableList<DateResponseItem>, positionChanged: Int? = null) {
        val diferencia = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = data.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = data[oldItemPosition]
                val newItem = newList[newItemPosition]
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newList[newItemPosition]
            }
        })

        this.data = newList
        diferencia.dispatchUpdatesTo(this)
        positionChanged?.let {
            notifyItemChanged(positionChanged)
        } ?: run {
            notifyDataSetChanged()
        }
    }

    /** get the next index to make the call */
    fun getNextDate(date: String): Int? {
       val currentIndex =  data.withIndex().find { it.value.date == date }?.index
        currentIndex?.let {
            return currentIndex + 1
        } ?: return null
    }
}