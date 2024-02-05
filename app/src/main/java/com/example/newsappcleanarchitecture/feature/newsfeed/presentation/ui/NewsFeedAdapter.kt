package com.example.newsappcleanarchitecture.feature.newsfeed.presentation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsappcleanarchitecture.R
import com.example.newsappcleanarchitecture.databinding.ItemNewsBinding
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.domainClass.News

class NewsFeedAdapter : RecyclerView.Adapter<NewsFeedAdapter.NewsItemViewHolder>() {

    private var mNewsList = emptyList<News>()
    private lateinit var itemNewsBinding: ItemNewsBinding
    private var onItemClick: ((News) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setNewsList(
        mNewsList: List<News>,
    ) {
        this.mNewsList = mNewsList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setOnItemClick(onItemClick: ((News) -> Unit)? = null) {
        this.onItemClick = onItemClick
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        itemNewsBinding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(itemNewsBinding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bind(mNewsList[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    inner class NewsItemViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News, onItemClick: ((News) -> Unit)? = null) {
            with(binding) {
                Glide.with(root.context).load(news.image).apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                ).into(image)
                title.text = news.title
                description.text = news.description
                binding.root.setOnClickListener { onItemClick?.invoke(news) }
            }
        }
    }
}