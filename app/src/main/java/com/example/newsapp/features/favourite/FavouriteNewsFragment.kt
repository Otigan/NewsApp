package com.example.newsapp.features.favourite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.NewsAdapter
import com.example.newsapp.databinding.FragmentFavouriteNewsBinding
import com.example.newsapp.models.Article
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                lifecycleScope.launch(Dispatchers.IO) {
                    val list = favouriteNewsViewModel.listOfLiked()
                    val position = viewHolder.bindingAdapterPosition
                    val article = list[position]
                    favouriteNewsViewModel.removeLike(article.url)
                    withContext(Dispatchers.Main) {
                        //Toast.makeText(requireContext(), article.title, Toast.LENGTH_SHORT).show()
                        Snackbar.make(view, "Like removed", Snackbar.LENGTH_SHORT).apply {
                            setAction("Undo") {
                                favouriteNewsViewModel.setLike(article.url)
                            }
                        }.show()
                    }
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    override fun onItemClick(article: Article) {
        Toast.makeText(context, "ON ITEM CLICK", Toast.LENGTH_SHORT).show()
    }
}