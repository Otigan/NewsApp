package com.example.newsapp.news

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newsapp.R
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {

    private val newsViewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentNewsBinding? = null

    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNewsBinding.bind(view)

        val adapter = NewsAdapter()

        binding.apply {
            newsRecyclerView.setHasFixedSize(true)
            newsRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                footer = NewsPhotoLoadStateAdapter {
                    adapter.retry()
                },
                header = NewsPhotoLoadStateAdapter {
                    adapter.retry()
                }
            )
        }

        newsViewModel.headlines.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        setHasOptionsMenu(true)
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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}