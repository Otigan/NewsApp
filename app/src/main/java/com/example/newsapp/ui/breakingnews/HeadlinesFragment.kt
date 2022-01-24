package com.example.newsapp.ui.breakingnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.presentation.HeadlinesViewModel
import com.example.newsapp.presentation.NetworkStatusViewModel
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.adapter.NewsLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    private var _binding: FragmentHeadlinesBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
    private val headlinesViewModel by viewModels<HeadlinesViewModel>()

    @ExperimentalCoroutinesApi
    private val networkStatusViewModel by activityViewModels<NetworkStatusViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlinesBinding.inflate(inflater)
        return binding.root
    }


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsAdapter { article ->
            navigateToDetailedNews(article)
        }

        binding.apply {
            topHeadlinesRecyclerView.apply {
                setHasFixedSize(true)
                adapter = newsAdapter.withLoadStateHeaderAndFooter(
                    header = NewsLoadStateAdapter { newsAdapter.retry() },
                    footer = NewsLoadStateAdapter { newsAdapter.retry() }
                )
            }
            btnRetry.setOnClickListener {
                newsAdapter.retry()
            }
        }

        getResults()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsAdapter.loadStateFlow.collect { loadState ->
                    val isListEmpty =
                        loadState.refresh is LoadState.Error && newsAdapter.itemCount == 0

                    binding.apply {
                        textViewError.isVisible = isListEmpty
                        topHeadlinesRecyclerView.isVisible =
                            loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                        progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                        btnRetry.isVisible =
                            loadState.mediator?.refresh is LoadState.Error && newsAdapter.itemCount == 0
                    }

                    val errorState = loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error

                    Log.d("HeadlinesFragment", "errorState:${errorState?.error} ")

                    errorState?.let {
                        Toast.makeText(
                            context,
                            "\uD83D\uDE28 Wooops ${it.error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                headlinesViewModel.articles.collectLatest { articles ->
                    newsAdapter.submitData(articles)
                }
            }
        }
    }

    private fun navigateToDetailedNews(article: ArticleDto) {
        val action =
            HeadlinesFragmentDirections.actionHeadlinesFragmentToDetailNewsFragment(article)

        findNavController().navigate(action)
    }
}