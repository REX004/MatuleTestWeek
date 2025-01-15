package com.amirx.matule_app_sessions.ui.main.home

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
import com.amirx.matule_app_sessions.databinding.FragmentHomeBinding
import com.amirx.matule_app_sessions.databinding.FragmentPopularProductsBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import com.amirx.matule_app_sessions.ui.main.home.adapters.CategoryAdapter
import com.amirx.matule_app_sessions.ui.main.home.adapters.ProductAdapter


class PopularProductsFragment : BaseFragment() {

    private val binding: FragmentPopularProductsBinding by lazy {
        FragmentPopularProductsBinding.inflate(
            layoutInflater
        )
    }
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun applyClick() {
        super.applyClick()
        binding.backBt.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.favoriteBt.setOnClickListener {
            findNavController().navigate(R.id.favoritesFragment)
        }
    }


    override fun observeAdapters() {
        super.observeAdapters()
        try {
            binding.popularRv.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.popularRv.adapter = popularAdapter

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