package com.example.newsapp.ui.searchnews

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.presentation.NetworkStatusViewModel
import com.example.newsapp.presentation.SearchNewsViewModel
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.adapter.NewsLoadStateAdapter
import com.example.newsapp.util.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_news) {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val searchNewsViewModel by viewModels<SearchNewsViewModel>()
    @ExperimentalCoroutinesApi
    private val networkStatusViewModel by activityViewModels<NetworkStatusViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val newsAdapter = NewsAdapter { article ->
            navigateToDetailedNews(article)
        }

        binding.apply {
            newsRecyclerView.apply {
                setHasFixedSize(true)
                adapter = newsAdapter.withLoadStateHeaderAndFooter(
                    header = NewsLoadStateAdapter { newsAdapter.retry() },
                    footer = NewsLoadStateAdapter { newsAdapter.retry() }
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchNewsViewModel.searchResults.collectLatest { data ->
                    newsAdapter.submitData(data)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_news, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.newsRecyclerView.scrollToPosition(0)
                    searchNewsViewModel.submitSearchQuery(it)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {

            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToDetailedNews(article: ArticleDto) {
        val action =
            SearchNewsFragmentDirections.actionSearchNewsFragmentToDetailNewsFragment(article)

        findNavController().navigate(action)
    }

}