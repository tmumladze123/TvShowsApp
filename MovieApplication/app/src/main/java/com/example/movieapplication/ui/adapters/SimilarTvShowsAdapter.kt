package com.example.movieapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.GetResponseUrl
import com.example.movieapplication.databinding.SimilarTvShowItemBinding
import com.example.movieapplication.extensions.setImage
import com.example.movieapplication.model.TvShowItem

class SimilarTvShowsAdapter : RecyclerView.Adapter<SimilarTvShowsAdapter.SimilarTvShowItemViewHolder>()  {
    var similarTvShows: List<TvShowItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onItemClick: ((TvShowItem) -> Unit)? = null
    inner class SimilarTvShowItemViewHolder(private val binding: SimilarTvShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentItem: TvShowItem
        fun onBind() {
            currentItem = similarTvShows[adapterPosition]
            binding.tvName.text = currentItem.name.toString()
            binding.ivPoster.setImage(GetResponseUrl.IMAGEPATH + currentItem.posterPath)
            binding.root.setOnClickListener {
                onItemClick?.invoke(currentItem)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarTvShowItemViewHolder =
        SimilarTvShowItemViewHolder(SimilarTvShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SimilarTvShowItemViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = similarTvShows.size

}