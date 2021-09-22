package com.example.newsapp.features.news

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
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.api.Article
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news), NewsAdapter.OnItemClickListener {

    private val newsViewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentNewsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNewsBinding.bind(view)

        adapter = NewsAdapter(this)

        binding.apply {
            newsRecyclerView.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = this@NewsFragment.adapter.withLoadStateHeaderAndFooter(
                    footer = NewsPhotoLoadStateAdapter {
                        this@NewsFragment.adapter.retry()
                    },
                    header = NewsPhotoLoadStateAdapter {
                        this@NewsFragment.adapter.retry()
                    }
                )
                btnRetry.setOnClickListener {
                    this@NewsFragment.adapter.retry()
                }
            }
        }

        newsViewModel.allNews.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                newsRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error


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
            NewsFragmentDirections.actionNewsFragmentToDetailedNewsFragment(article, article.title)
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
                    newsViewModel.getHeadlines(query)
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
                Toast.makeText(context, "refresh", Toast.LENGTH_SHORT).show()
                newsViewModel.getHeadlines()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}