package com.amirx.matule_app_sessions.ui.main.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Cart
import com.amirx.matule_app_sessions.databinding.FragmentCartBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import com.amirx.matule_app_sessions.ui.main.cart.adapters.CartAdapter
import kotlinx.coroutines.launch

class CartFragment : BaseFragment() {
    private val binding: FragmentCartBinding by lazy { FragmentCartBinding.inflate(layoutInflater) }
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        loadCart()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            requireContext(),
            onQuantityChange = { cart, newQuantity ->
                viewModel.updateQuantity(cart, newQuantity)
                loadCart() // Обновляем список после изменения количества
            },
            onDeleteClick = { cart ->
                viewModel.deleteFromCart(cart)
                loadCart() // Обновляем список после удаления
            }
        )

        binding.productsRv.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewModel.products.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainContainer.visibility = View.VISIBLE
                    cartAdapter.submitList(response.data)
                    Log.d(
                        "com.amirx.matule_app_sessions.ui.main.cart.CartFragment",
                        "Observer: submitList called with ${response.data.size} items"
                    )
                    updateTotals(response.data)
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

    override fun applyClick() {
        super.applyClick()
        binding.checkout.setOnClickListener {
            findNavController().navigate(R.id.checkoutFragment)
        }
    }

    private fun setupClickListeners() {
        binding.checkout.setOnClickListener {
        }

        binding.backBt.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun updateTotals(cartItems: List<Cart>) {
        val subtotal = cartItems.sumOf { it.product.price!! * it.quantity!! }
        val delivery = 60.20
        val total = subtotal + delivery

        binding.apply {
            sumPrice.text = "₽${String.format("%.2f", subtotal)}"
            sumDelivery.text = "₽${String.format("%.2f", delivery)}"
            itogTxt.text = "₽${String.format("%.2f", total)}"
            quantityTxt.text = "${cartItems.size} товара"
        }
    }

    private fun loadCart() {
        lifecycleScope.launch {
            viewModel.getProducts()
        }
    }
}