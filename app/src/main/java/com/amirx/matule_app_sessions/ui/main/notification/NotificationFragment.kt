package com.amirx.matule_app_sessions.ui.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.databinding.FragmentNotificationBinding
import com.amirx.matule_app_sessions.domain.usecase.CustomConfirmDialog
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import com.amirx.matule_app_sessions.ui.main.notification.adapters.NotificationAdapter


class NotificationFragment : BaseFragment() {


    private val viewModel: NotificationViewModel by viewModels(factoryProducer = {
        NotificationViewModelProvider()
    })
    private val binding: FragmentNotificationBinding by lazy {
        FragmentNotificationBinding.inflate(
            layoutInflater
        )
    }

    private val notificationAdapter: NotificationAdapter by lazy { NotificationAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun observeAdapters() {
        super.observeAdapters()
        binding.notificationsRv.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun observeViewModel() {
        super.observeViewModel()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Loading -> {
                    binding.mainContainer.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResponseState.Success -> {
                    binding.mainContainer.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    notificationAdapter.submitList(state.data)
                }

                is ResponseState.Error -> {
                    binding.mainContainer.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    showErrorDialog(state.message)
                }

                else -> {

                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        CustomConfirmDialog(requireContext(), "Error", message, {
            null
        }).show()
    }

    override fun applyClick() {
        super.applyClick()

    }

}