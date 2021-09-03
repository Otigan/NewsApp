package com.example.newsapp.features.headlines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newsapp.R
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    private val topHeadlinesViewModel by viewModels<TopHeadlinesViewModel>()

    private var _binding: FragmentHeadlinesBinding? = null

    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHeadlinesBinding.bind(view)

        val adapter = NewsAdapter()

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

    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}