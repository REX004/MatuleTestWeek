package com.amirx.matule_app_sessions.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun applyClick() {
    }

    open fun loadImage() {

    }

    open fun observeAdapters() {

    }

    open fun observeViewModel() {
    }

    open fun setupObserves() {

    }

    open fun stateCheck() {

    }

    open fun onBackPressed(){
        requireActivity().onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyClick()
        observeViewModel()
        loadImage()
        stateCheck()
        setupObserves()
        observeAdapters()
    }
}