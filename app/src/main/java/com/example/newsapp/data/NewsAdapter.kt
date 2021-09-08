package com.example.newsapp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.R
import com.example.newsapp.api.Articles
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Articles, NewsAdapter.NewsViewHolder>(NEWS_COMPARATOR) {


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsViewHolder(binding)
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(article: Articles) {
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_launcher_background)
                    .into(newsImage)

                articleTitle.text = article.title
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(article: Articles)
    }

    companion object {
        private val NEWS_COMPARATOR = object : DiffUtil.ItemCallback<Articles>() {
            override fun areItemsTheSame(oldItem: Articles, newItem: Articles) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Articles, newItem: Articles) =
                oldItem == newItem
        }
    }
}