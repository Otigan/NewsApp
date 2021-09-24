package com.example.newsapp.features.headlines

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.api.Article
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.features.news.NewsPhotoLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines), NewsAdapter.OnItemClickListener {

    private val topHeadlinesViewModel by viewModels<TopHeadlinesViewModel>()

    private var _binding: FragmentHeadlinesBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHeadlinesBinding.bind(view)

        adapter = NewsAdapter(this)

        binding.apply {
            topHeadlinesRecyclerView.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = this@HeadlinesFragment.adapter.withLoadStateHeaderAndFooter(
                    footer = NewsPhotoLoadStateAdapter {
                        this@HeadlinesFragment.adapter.retry()
                    },
                    header = NewsPhotoLoadStateAdapter {
                        this@HeadlinesFragment.adapter.retry()
                    }
                )
                btnRetry.setOnClickListener {
                    this@HeadlinesFragment.adapter.retry()
                }
            }
        }

        topHeadlinesViewModel.headlines().observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                topHeadlinesRecyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error
            }
        }

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_headlines, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_refresh -> {
                topHeadlinesViewModel.headlines().observe(viewLifecycleOwner) {
                    adapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(article: Article) {
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