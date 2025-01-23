package com.amirx.matule_app_sessions.ui.main.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.databinding.FragmentCheckoutBinding
import com.amirx.matule_app_sessions.domain.usecase.SuccessfullDialog
import com.amirx.matule_app_sessions.ui.base.BaseFragment

class CheckoutFragment : BaseFragment() {

    private val binding: FragmentCheckoutBinding by lazy {
        FragmentCheckoutBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun applyClick() {
        super.applyClick()
        binding.checkout.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        SuccessfullDialog(requireContext(), resources.getString(R.string.success_text)) {
            findNavController().navigate(R.id.homeFragment)
        }.show()
    }


}