package com.example.newsapp.features.headlines

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.api.Articles
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines), NewsAdapter.OnItemClickListener {

    private val topHeadlinesViewModel by viewModels<TopHeadlinesViewModel>()

    private var _binding: FragmentHeadlinesBinding? = null

    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHeadlinesBinding.bind(view)

        val adapter = NewsAdapter(this)

        binding.apply {
            topHeadlinesRecyclerView.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = adapter
            }
        }

        topHeadlinesViewModel.headlines().observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                topHeadlinesRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error



            }
        }

    }

    override fun onItemClick(article: Articles) {
        val action = HeadlinesFragmentDirections.actionHeadlinesFragmentToDetailedNewsFragment(
            article,
            article.title
        )
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}