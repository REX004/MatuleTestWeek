package com.amirx.matule_app_sessions.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.data.datasource.local.SharedPrefsManager
import com.amirx.matule_app_sessions.databinding.FragmentOnboardingBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment


class OnboardingFragment : BaseFragment() {

    val imageResources = listOf(
        R.drawable.status_1, R.drawable.status_2, R.drawable.status_3
    )
    private val adapter: OnBoardingAdapter by lazy { OnBoardingAdapter(requireFragmentManager()) }
    private val binding: FragmentOnboardingBinding by lazy {
        FragmentOnboardingBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val check = SharedPrefsManager(requireContext()).checkOnboarding()
        if (check == true) {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun observeAdapters() {
        super.observeAdapters()
        binding.viewPager.adapter = adapter
    }

    override fun applyClick() {
        super.applyClick()
        binding.materialButton.setOnClickListener {
            if (binding.viewPager.currentItem == 2) {
                SharedPrefsManager(requireContext()).saveStateOnboarding(true)
                findNavController().popBackStack()
                findNavController().navigate(R.id.homeFragment)
            } else {
                binding.viewPager.currentItem += 1
            }
        }
    }

    override fun setupObserves() {
        super.setupObserves()
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                updateButtonText()
                updateStatusImage()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun updateStatusImage() {
        binding.status.setImageResource(imageResources[binding.viewPager.currentItem])
    }

    private fun updateButtonText() {
        val start = getText(R.string.start)
        val next = getText(R.string.next)
        binding.materialButton.text = when (binding.viewPager.currentItem) {
            0 -> start
            1 -> next
            2 -> next
            else -> {
                next
            }
        }
    }

}