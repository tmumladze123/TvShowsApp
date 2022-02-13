package com.example.movieapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.GetResponseUrl
import com.example.movieapplication.databinding.TvShowItemBinding
import com.example.movieapplication.extensions.setImage
import com.example.movieapplication.model.TvShowItem


class TvShowsPagingAdapter : PagingDataAdapter<TvShowItem,
        TvShowsPagingAdapter.TvShowItemHolder>(diffCallback) {


    var onItemClick: ((TvShowItem) -> Unit)? = null

    inner class TvShowItemHolder(
        val binding: TvShowItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<TvShowItem>() {
            override fun areItemsTheSame(oldItem: TvShowItem, newItem: TvShowItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShowItem, newItem: TvShowItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowItemHolder {
        return TvShowItemHolder(
            TvShowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TvShowItemHolder, position: Int) {
        val currChar = getItem(position)

        holder.binding.apply {

                tvName.text = currChar?.name.toString()
                tvRating.text = currChar?.voteAverage.toString()
                imTvShow.setImage(GetResponseUrl.IMAGEPATH + currChar?.posterPath)
                root.setOnClickListener {
                    onItemClick?.invoke(currChar!!)
                }


        }

    }


}