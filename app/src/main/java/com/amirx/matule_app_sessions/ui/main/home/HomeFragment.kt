package com.amirx.matule_app_sessions.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amirx.matule_app_sessions.databinding.FragmentHomeBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment

class HomeFragment : BaseFragment() {

    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }




}