package com.example.newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(private val onClick: (article: ArticleDto) -> Unit) :
    PagingDataAdapter<ArticleDto, NewsAdapter.NewsViewHolder>(ARTICLE_COMPARATOR) {

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<ArticleDto>() {
            override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean =
                oldItem == newItem


            override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean =
                oldItem.title == newItem.title
        }
    }


    class NewsViewHolder(
        private val binding: ItemNewsBinding,
        private val onClick: (article: ArticleDto) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleDto) {
            binding.apply {
                root.setOnClickListener {
                    onClick(article)
                }
                Glide.with(root)
                    .load(article.urlToImage)
                    .into(newsImage)
                articleTitle.text = article.title
            }
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, onClick)
    }
}
