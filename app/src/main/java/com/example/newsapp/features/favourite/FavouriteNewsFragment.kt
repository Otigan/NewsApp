package com.example.newsapp.features.favourite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.databinding.FragmentFavouriteNewsBinding
import com.example.newsapp.models.Article
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteNewsFragment : Fragment(R.layout.fragment_favourite_news),
    NewsAdapter.OnItemClickListener {

    private var _binding: FragmentFavouriteNewsBinding? = null
    private val binding get() = _binding!!
    private val favouriteNewsViewModel by viewModels<FavouriteNewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFavouriteNewsBinding.bind(view)

        val newsAdapter = NewsAdapter(this)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            itemAnimator = null
            adapter = newsAdapter
        }

        favouriteNewsViewModel.likedNews.observe(viewLifecycleOwner, {
            newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Snackbar.make(view, "Like removed", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(article: Article) {
        Toast.makeText(context, "ON ITEM CLICK", Toast.LENGTH_SHORT).show()
    }
}