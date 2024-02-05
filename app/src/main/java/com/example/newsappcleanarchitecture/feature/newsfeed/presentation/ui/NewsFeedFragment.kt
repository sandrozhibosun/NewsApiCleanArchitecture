package com.example.newsappcleanarchitecture.feature.newsfeed.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappcleanarchitecture.R
import com.example.newsappcleanarchitecture.databinding.FragmentNewsFeedBinding
import com.example.newsappcleanarchitecture.feature.newsfeed.presentation.viewmodel.NewsFeedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewsFeedFragment : Fragment() {

    private var _binding: FragmentNewsFeedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: NewsFeedViewModel by viewModels()

    private val newsFeedAdapter by lazy { NewsFeedAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewsFeedBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = newsFeedAdapter
        }
    }

    private fun setupListeners() {
        newsFeedAdapter.setOnItemClick { news ->
            findNavController().navigate(
                R.id.action_NewsFeedFragment_to_NewsDetailFragment,
                bundleOf(
                    "NewsArg" to news
                )
            )
        }
        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshNews()
        }
    }

    private fun setupObservers() {
        viewModel.newsState.flowWithLifecycle(lifecycle)
            .onEach {
                newsFeedAdapter.setNewsList(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.errorState.flowWithLifecycle(lifecycle)
            .onEach { errorDescription ->
                errorDescription?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.loadingState.flowWithLifecycle(lifecycle)
            .onEach {
                binding.swipeContainer.isRefreshing = it
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}