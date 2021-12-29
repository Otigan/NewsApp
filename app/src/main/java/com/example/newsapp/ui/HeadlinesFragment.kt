package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.presentation.HeadlinesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    private var _binding: FragmentHeadlinesBinding? = null
    private val binding get() = _binding!!
    private val headlinesViewModel by viewModels<HeadlinesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlinesBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsAdapter = NewsAdapter()

        newsAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                topHeadlinesRecyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    newsAdapter.itemCount < 1
                ) {
                    topHeadlinesRecyclerView.isVisible = false
                    textViewError.isVisible = true
                } else {
                    textViewError.isVisible = false
                }
            }
        }

        binding.apply {
            topHeadlinesRecyclerView.apply {
                setHasFixedSize(true)
                adapter = newsAdapter
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            headlinesViewModel.articles.collectLatest(newsAdapter::submitData)
        }

        /*viewLifecycleOwner.lifecycleScope.launch {
            headlinesViewModel.headlinesFlow.collect { event ->
                when (event) {
                    is HeadlinesEvent.Loading -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is HeadlinesEvent.Error -> {
                        Snackbar.make(binding.root, event.errorMessage, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is HeadlinesEvent.Success -> {
                        newsAdapter.submitData(event.headlines)
                    }
                }
            }
        }*/
    }

}