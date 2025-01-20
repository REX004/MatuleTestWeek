package com.amirx.matule_app_sessions.ui.main.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amirx.matule_app_sessions.databinding.FragmentCheckoutBinding
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
}