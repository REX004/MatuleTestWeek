package com.amirx.matule_app_sessions.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.amirx.matule_app_sessions.ui.onboarding.fragments.Onboarding1Fragment
import com.amirx.matule_app_sessions.ui.onboarding.fragments.Onboarding2Fragment
import com.amirx.matule_app_sessions.ui.onboarding.fragments.Onboarding3Fragment
import kotlin.jvm.Throws

class OnBoardingAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Onboarding1Fragment()
            1 -> Onboarding2Fragment()
            2 -> Onboarding3Fragment()
            else -> {
                throw RuntimeException("Failed to get fragment")
            }
        }
    }
}