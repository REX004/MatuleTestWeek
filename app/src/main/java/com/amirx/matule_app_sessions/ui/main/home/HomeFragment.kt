package com.amirx.matule_app_sessions.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.databinding.FragmentHomeBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import com.amirx.matule_app_sessions.ui.main.home.adapters.CategoryAdapter
import com.amirx.matule_app_sessions.ui.main.home.adapters.ProductAdapter

class HomeFragment : BaseFragment() {

    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val popularAdapter by lazy {
        ProductAdapter(requireContext()) { product ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product)
            findNavController().navigate(action)
        }
    }

    private val viewModel: HomeViewModel by viewModels(factoryProducer = {
        HomeViewModelProvider()
    })
    private val categoryAdapter by lazy {
        CategoryAdapter { category ->
//            val action =
//                HomeFragmentDirections.actionHomeFragmentToCategoryDetailFragment(category)
//            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun applyClick() {
        super.applyClick()
        binding.allBt.setOnClickListener {
            findNavController().navigate(R.id.popularProductsFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPopularProducts()
        viewModel.getProducts()
    }

    override fun observeAdapters() {
        super.observeAdapters()
        try {
            binding.popularRv.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.popularRv.adapter = popularAdapter

            binding.categoryRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.categoryRv.adapter = categoryAdapter
            Log.d("HomeFragment", "Successful setupUI")

        } catch (e: Exception) {
            Log.e("HomeFragment", "Failed setupUI")

        }
    }

    override fun setupObserves() {
        super.setupObserves()
        viewModel.products.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Loading -> {
                    binding.popularRv.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResponseState.Success -> {
                    binding.popularRv.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    Log.d("HomeFragment", "Products received: ${state.data}")
                    categoryAdapter.submitList(state.data)
                }

                is ResponseState.Error -> {
                    binding.popularRv.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.popularProducts.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Loading -> {
                    binding.popularRv.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResponseState.Success -> {
                    binding.popularRv.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    Log.d("HomeFragment", "Products received: ${state.data}")
                    popularAdapter.submitList(state.data)
                }

                is ResponseState.Error -> {
                    binding.popularRv.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

}