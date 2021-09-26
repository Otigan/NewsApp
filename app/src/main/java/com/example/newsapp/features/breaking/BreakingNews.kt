package com.example.newsapp.features.breaking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.example.newsapp.R
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.models.Article
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalPagingApi
@AndroidEntryPoint
class BreakingNews : Fragment(R.layout.fragment_breaking_news), NewsAdapter.OnItemClickListener {


    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!
    private val breakingNewsViewModel by viewModels<BreakingNewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBreakingNewsBinding.bind(view)

        val newsAdapter = NewsAdapter(this)

        binding.topHeadlinesRecyclerView.apply {
            setHasFixedSize(true)
            itemAnimator = null
            adapter = newsAdapter
        }

        breakingNewsViewModel.getBreakingNews().observe(viewLifecycleOwner, {
            newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })


    }

    override fun onItemClick(article: Article) {
        val action = BreakingNewsDirections.actionHeadlinesFragmentToDetailedNewsFragment(article)
        findNavController().navigate(action)
    }
}