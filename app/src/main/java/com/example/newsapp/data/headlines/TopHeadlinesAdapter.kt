package com.example.newsapp.data.headlines

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

class TopHeadlinesAdapter :
    PagingDataAdapter<Articles, TopHeadlinesAdapter.TopHeadlinesViewHolder>(HEADLINES_COMPARATOR) {

    class TopHeadlinesViewHolder(private val binging: ItemNewsBinding) :
        RecyclerView.ViewHolder(binging.root) {

        fun bind(articles: Articles) {
            binging.apply {
                articleTitle.text = articles.title
                Glide.with(itemView)
                    .load(articles.urlToImage)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_launcher_background)
                    .into(newsImage)
            }
        }

    }

    override fun onBindViewHolder(holder: TopHeadlinesViewHolder, position: Int) {

        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopHeadlinesViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TopHeadlinesViewHolder(binding)
    }

    companion object {
        private val HEADLINES_COMPARATOR = object : DiffUtil.ItemCallback<Articles>() {

            override fun areItemsTheSame(oldItem: Articles, newItem: Articles) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Articles, newItem: Articles) =
                oldItem == newItem

        }
    }
}