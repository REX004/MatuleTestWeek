package com.amirx.matule_app_sessions.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.databinding.FragmentLoginBinding
import com.amirx.matule_app_sessions.domain.usecase.CustomConfirmDialog
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private val binding: FragmentLoginBinding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels(factoryProducer = {
        LoginViewModelProvider()
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        applyClick()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginState.Error -> {
                    binding.mainContainer.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    showErrorDialog(state.message)

                }

                is LoginState.Success -> {
                    binding.mainContainer.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.homeFragment)
                }

                is LoginState.Loading -> {
                    binding.mainContainer.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        CustomConfirmDialog(requireContext(), "Error", message, {
            null
        }).show()
    }

    private fun applyClick() {
        binding.loginBt.setOnClickListener {
            lifecycleScope.launch {
                viewModel.loginUser(binding.emailET.text.toString(), binding.passwordET.text.toString(), requireContext())
            }
        }
    }

}