package com.example.newsapp.ui.breakingnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.presentation.HeadlinesViewModel
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.adapter.NewsLoadStateAdapter
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

        val newsAdapter = NewsAdapter { article ->
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
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                headlinesViewModel.articles.collectLatest { articles ->
                    newsAdapter.submitData(articles)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToDetailedNews(article: ArticleDto) {
        val action =
            HeadlinesFragmentDirections.actionHeadlinesFragmentToDetailNewsFragment(article)

        findNavController().navigate(action)
    }
}