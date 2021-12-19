package com.example.newsapp.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentDetailsBinding
import com.example.newsapp.presentation.detailed.DetailedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedNewsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailedNewsFragmentArgs>()
    private val detailedViewModel by viewModels<DetailedViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding = FragmentDetailsBinding.bind(view)

        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }


        binding.fab.setOnClickListener {
            detailedViewModel.setLike(article.url)
            Snackbar.make(view, "Article is liked", Snackbar.LENGTH_SHORT).show()
        }
    }

}