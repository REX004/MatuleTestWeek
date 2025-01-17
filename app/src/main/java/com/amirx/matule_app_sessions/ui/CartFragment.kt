package com.amirx.matule_app_sessions.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.databinding.FragmentCartBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment


class CartFragment : BaseFragment() {

    private val binding: FragmentCartBinding by lazy { FragmentCartBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }


}