package com.example.newsapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.data.remote.models.Article
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.presentation.news.NewsPhotoLoadStateAdapter
import com.example.newsapp.presentation.news.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news), NewsAdapter.OnItemClickListener {

    private val newsViewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentNewsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: NewsAdapter


    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNewsBinding.bind(view)

        adapter = NewsAdapter(this)

        newsViewModel.searchedNews.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        binding.apply {
            newsRecyclerView.apply {
                setHasFixedSize(true)
                itemAnimator = null
                adapter = this@NewsFragment.adapter.withLoadStateHeaderAndFooter(
                    header = NewsPhotoLoadStateAdapter { this@NewsFragment.adapter.retry() },
                    footer = NewsPhotoLoadStateAdapter { this@NewsFragment.adapter.retry() },
                )

            }
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                newsRecyclerView.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.mediator?.refresh is LoadState.Error
                textViewError.isVisible = loadState.mediator?.refresh is LoadState.Error

                if (loadState.mediator?.refresh is LoadState.Error) {
                    Toast.makeText(
                        context,
                        (loadState.mediator?.refresh as LoadState.Error).error.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // If no results from query
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    newsRecyclerView.isVisible = false
                    textViewNoResults.isVisible = true
                } else {
                    textViewNoResults.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(article: Article) {
        val action =
            NewsFragmentDirections.actionNewsFragmentToDetailedNewsFragment(
                article
            )
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_news, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.newsRecyclerView.scrollToPosition(0)
                    newsViewModel.changeQuery(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_refresh -> {
                newsViewModel.changeQuery(newsViewModel.currentQuery.value.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}