package com.amirx.matule_app_sessions.ui.main.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.databinding.FragmentProfileBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment


class ProfileFragment : BaseFragment() {

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(
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

    }
}