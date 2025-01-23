package com.amirx.matule_app_sessions.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.databinding.FragmentCategoryDetailBinding
import com.amirx.matule_app_sessions.databinding.FragmentHomeBinding
import com.amirx.matule_app_sessions.databinding.FragmentPopularProductsBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import com.amirx.matule_app_sessions.ui.main.home.HomeFragmentDirections
import com.amirx.matule_app_sessions.ui.main.home.HomeViewModel
import com.amirx.matule_app_sessions.ui.main.home.HomeViewModelProvider
import com.amirx.matule_app_sessions.ui.main.home.adapters.CategoryAdapter
import com.amirx.matule_app_sessions.ui.main.home.adapters.ProductAdapter


class CategoryDetailFragment : BaseFragment() {

    private val binding: FragmentCategoryDetailBinding by lazy {
        FragmentCategoryDetailBinding.inflate(layoutInflater)
    }

    private val productAdapter by lazy {
        ProductAdapter(requireContext()) { product ->
            val action =
                CategoryDetailFragmentDirections.actionCategoryDetailFragmentToProductDetailFragment(
                    product
                )
            findNavController().navigate(action)
        }
    }

    private val viewModel: HomeViewModel by viewModels(factoryProducer = {
        HomeViewModelProvider()
    })

    private val category by lazy {
        arguments?.let { CategoryDetailFragmentArgs.fromBundle(it).category }
            ?: "" // Получаем переданную категорию
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryTitle.text = category

        viewModel.getCategoryProducts(category)
    }

    override fun observeAdapters() {
        super.observeAdapters()
        try {
            binding.popularRv.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.popularRv.adapter = productAdapter
        } catch (e: Exception) {
            Log.e("CategoryDetailFragment", "Failed setupUI")
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
                    Log.d("CategoryDetailFragment", "Products received: ${state.data}")
                    productAdapter.submitList(state.data)
                }

                is ResponseState.Error -> {
                    binding.popularRv.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}