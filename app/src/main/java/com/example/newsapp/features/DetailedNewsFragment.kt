package com.example.newsapp.features

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentDetailsBinding

class DetailedNewsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailedNewsFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            val article = args.article

            Glide.with(this@DetailedNewsFragment)
                .load(article.urlToImage)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewTitle.isVisible = true
                        textViewDesc.isVisible = true
                        return false
                    }
                })
                .into(imgViewDetails)

            textViewTitle.text = article.title
            textViewDesc.text = article.description

            val uri = Uri.parse(article.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            textViewFullArticle.apply {
                val url = article.url
                text = "Full article: $url"
                setOnClickListener {
                    context.startActivity(intent)
                }
                paint.isUnderlineText = true
            }
        }
    }
}