package com.amirx.matule_app_sessions.ui.product

import CartAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Cart
import com.amirx.matule_app_sessions.databinding.FragmentOrdersBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment

import kotlinx.coroutines.launch

class OrdersFragment : BaseFragment() {
    private val binding: FragmentOrdersBinding by lazy {
        FragmentOrdersBinding.inflate(layoutInflater)
    }
    private val viewModel: CartViewModell by viewModels(factoryProducer = { CartViewModellFactory() })

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        loadCart()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            requireContext(),
            onQuantityChange = { cart, newQuantity ->
                loadCart()
            },
            onDeleteClick = { cart ->
                loadCart()
            }
        )

        binding.productsRv.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.cartState.collect { response ->
                when (response) {
                    is ResponseState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.mainContainer.visibility = View.VISIBLE

                        // Группировка элементов корзины
                        cartAdapter.submitGroupedList(
                            response.data.filterIsInstance<CartGroupItem.Item>()
                                .map { it.cart }
                        )

                        updateTotals(
                            response.data.filterIsInstance<CartGroupItem.Item>()
                                .map { it.cart }
                        )
                    }

                    is ResponseState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainContainer.visibility = View.GONE
                    }

                    is ResponseState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.mainContainer.visibility = View.VISIBLE
                        Toast.makeText(
                            requireContext(),
                            "Failed: ${response.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun applyClick() {
        super.applyClick()
        binding.backBt.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun updateTotals(cartItems: List<Cart>) {
        val subtotal = cartItems.sumOf { it.product.price!! * it.quantity!! }
        val delivery = 60.20
        val total = subtotal + delivery
    }

    private fun loadCart() {
        lifecycleScope.launch {
            viewModel.loadCart("some_user_id")
        }
    }
}