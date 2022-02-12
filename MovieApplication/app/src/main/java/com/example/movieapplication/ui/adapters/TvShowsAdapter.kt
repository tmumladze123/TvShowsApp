package com.example.movieapplication.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.GetResponseUrl.IMAGEPATH
import com.example.movieapplication.databinding.TvShowItemBinding
import com.example.movieapplication.extensions.setImage
import com.example.movieapplication.model.TvShowItem


class TvShowsAdapter : RecyclerView.Adapter<TvShowsAdapter.TvShowItemViewHolder>() {

    var tvShows: List<TvShowItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onItemClick: ((TvShowItem) -> Unit)? = null

    inner class TvShowItemViewHolder(private val binding: TvShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentItem: TvShowItem
        fun onBind() {
            currentItem = tvShows[adapterPosition]
            binding.tvName.text = currentItem.name.toString()
            binding.tvRating.text = currentItem.voteAverage.toString()
            binding.imTvShow.setImage(IMAGEPATH + currentItem.posterPath)
            binding.root.setOnClickListener {
                onItemClick?.invoke(currentItem)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowItemViewHolder =
        TvShowItemViewHolder(TvShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TvShowItemViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = tvShows.size


}