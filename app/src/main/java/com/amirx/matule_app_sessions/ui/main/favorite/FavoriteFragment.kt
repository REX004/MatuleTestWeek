package com.amirx.matule_app_sessions.ui.main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.databinding.FragmentFavoriteBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import com.amirx.matule_app_sessions.ui.main.home.adapters.ProductAdapter

class FavoriteFragment : BaseFragment() {

    private val binding: FragmentFavoriteBinding by lazy {
        FragmentFavoriteBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelProvider()
    }
    private val popularAdapter by lazy {
        ProductAdapter(requireContext()) { product ->
            val action =
                FavoriteFragmentDirections.actionFavoritesFragmentToProductDetailFragment(product)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProducts()

    }

    override fun applyClick() {
        super.applyClick()
        binding.backBt.setOnClickListener {
            findNavController().navigate(R.id.cart)
        }
    }

    override fun observeAdapters() {
        super.observeAdapters()
        try {
            binding.productsRv.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.productsRv.adapter = popularAdapter

        } catch (e: Exception) {

        }
    }

    override fun setupObserves() {
        super.setupObserves()
        viewModel.products.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Loading -> {
                    binding.productsRv.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResponseState.Success -> {
                    binding.productsRv.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    popularAdapter.submitList(state.data)

                }

                is ResponseState.Error -> {
                    binding.productsRv.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}