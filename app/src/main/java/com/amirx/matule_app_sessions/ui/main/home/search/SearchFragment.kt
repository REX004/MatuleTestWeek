package com.amirx.matule_app_sessions.ui.main.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.room.Query
import com.amirx.matule_app_sessions.data.datasource.local.SharedPrefsManager
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.databinding.FragmentSearchBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import com.amirx.matule_app_sessions.ui.main.home.adapters.ProductAdapter
import com.amirx.matule_app_sessions.ui.main.home.search.adapter.SearchHistoryAdapter


class SearchFragment : BaseFragment() {
    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(
            layoutInflater
        )
    }
    private val searchHistoryAdapter: SearchHistoryAdapter by lazy {
        SearchHistoryAdapter(
            emptyList()
        ) { query ->
            binding.searchEt.setText(query)

        }
    }
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(requireContext(), {

        })
    }

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            SharedPrefsManager(
                requireContext()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun setupObserves() {
        super.setupObserves()
        viewModel.searchHistory.observe(viewLifecycleOwner) { history ->
            searchHistoryAdapter.submitList(history)
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Loading -> {
                    binding.popularProgressBar.visibility = View.VISIBLE
                    binding.productRv.visibility = View.GONE
                }

                is ResponseState.Success -> {
                    binding.popularProgressBar.visibility = View.GONE
                    binding.productRv.visibility = View.VISIBLE
                    productAdapter.submitList(state.data)
                    if (state.data.isEmpty()) {
                        Toast.makeText(requireContext(), "No such product", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is ResponseState.Error -> {
                    binding.popularProgressBar.visibility = View.GONE
                    binding.productRv.visibility = View.GONE
                }
            }
        }
    }

    private fun performSearch(query: String) {
        binding.searchHistory.visibility = View.GONE
        binding.productsContainer.visibility = View.VISIBLE
        viewModel.searchProducts(query)
    }

    override fun applyClick() {
        super.applyClick()
        binding.backBt.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}