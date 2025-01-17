package com.amirx.matule_app_sessions.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Product
import com.amirx.matule_app_sessions.data.repository.ProductRepository
import com.amirx.matule_app_sessions.databinding.FragmentDetailBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import com.amirx.matule_app_sessions.ui.detail.adapters.ImageIndicatorAdapter
import com.amirx.matule_app_sessions.ui.detail.adapters.ProductPagerAdapter

class ProductDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: ProductDetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels(
        factoryProducer =
        { DetailViewModelFactory(ProductRepository()) }
    )
    private lateinit var productPagerAdapter: ProductPagerAdapter
    private lateinit var indicatorAdapter: ImageIndicatorAdapter
    private var productList: List<Product> = emptyList()
    private var isDescriptionExpanded = false
    private var currentProduct: Product? = null
    private var isCurrentlyFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPopularProducts()
        setupViewPager()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.popularProducts.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Loading -> {
                }

                is ResponseState.Success -> {
                    productList = state.data
                    productPagerAdapter.submitList(productList)

                    setupIndicators(productList)

                    val initialPosition = productList.indexOfFirst { it.id == args.product.id }
                    if (initialPosition != -1) {
                        binding.crossMain.setCurrentItem(initialPosition, false)
                        updateDetails(productList[initialPosition])
                        indicatorAdapter.setSelectedPosition(initialPosition)
                        setupListeners()
                    } else if (productList.isNotEmpty()) {
                        binding.crossMain.setCurrentItem(0, false)
                        updateDetails(productList[0])
                        indicatorAdapter.setSelectedPosition(0)
                        setupListeners()
                    }
                }

                is ResponseState.Error -> {
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Loading -> {
                    Toast.makeText(
                        requireContext(),
                        "Saving/Deleting from favorites...",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ResponseState.Success<*> -> {
                    Toast.makeText(
                        requireContext(),
                        "Successfully saved/deleted from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateFavoriteButtonIcon()
                }

                is ResponseState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error saving/deleting from favorites: ${state.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateDetails(product: Product) {
        currentProduct = product
        binding.nameTxt.text = product.name
        binding.priceTxt.text = "${product.price} rub"
        binding.descriptionTxt.text = product.description
        binding.moreInfo.visibility =
            if (binding.descriptionTxt.lineCount >= 3 && !isDescriptionExpanded) View.VISIBLE else View.GONE
        binding.descriptionTxt.maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3

    }

    private fun setupViewPager() {
        productPagerAdapter = ProductPagerAdapter(requireContext())
        binding.crossMain.adapter = productPagerAdapter
    }

    override fun applyClick() {
        super.applyClick()
        binding.backBt.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.moreInfo.setOnClickListener {
            isDescriptionExpanded = true
            currentProduct?.let { updateDetails(it) }
            binding.moreInfo.visibility = View.GONE
        }
        binding.addToCartBt.setOnClickListener {
            viewModel.saveToCart(currentProduct!!, "some_user_id")
        }
        binding.favoriteBt.setOnClickListener {
            currentProduct?.let { product ->
                val userId = getCurrentUserId()
                if (isCurrentlyFavorite) {
                    viewModel.deleteFromFavorite(product, userId)
                } else {
                    viewModel.saveToFavorite(product, userId)
                }
                isCurrentlyFavorite = !isCurrentlyFavorite
                updateFavoriteButtonIcon()
            }
        }
    }

    private fun setupIndicators(productList: List<Product>) {
        indicatorAdapter = ImageIndicatorAdapter(requireContext(), productList)
        binding.itemsRv.adapter = indicatorAdapter
        binding.itemsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupListeners() {
        binding.crossMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (productList.isNotEmpty() && position < productList.size) {
                    updateDetails(productList[position])
                    indicatorAdapter.setSelectedPosition(position)
                }
                isDescriptionExpanded = false
                binding.moreInfo.visibility =
                    if (binding.descriptionTxt.lineCount >= 3) View.VISIBLE else View.GONE
                binding.descriptionTxt.maxLines = 3
            }
        })

        indicatorAdapter.setOnItemClickListener { position ->
            binding.crossMain.currentItem = position
        }
    }

    private fun getCurrentUserId(): String {
        return "some_user_id"
    }

    private fun updateFavoriteButtonIcon() {
        val icon =
            if (isCurrentlyFavorite) android.R.drawable.star_big_on else android.R.drawable.star_big_off
        binding.favoriteBt.setImageResource(icon)
    }
}