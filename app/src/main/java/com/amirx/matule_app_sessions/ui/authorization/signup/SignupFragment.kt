package com.amirx.matule_app_sessions.ui.authorization.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.data.datasource.local.SharedPrefsManager
import com.amirx.matule_app_sessions.databinding.FragmentLoginBinding
import com.amirx.matule_app_sessions.databinding.FragmentSignupBinding
import com.amirx.matule_app_sessions.domain.usecase.CustomConfirmDialog
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {

    private val binding: FragmentSignupBinding by lazy {
        FragmentSignupBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: SignupViewModel by viewModels(factoryProducer = {
        SignupViewModelProvider()
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
//        val check = SharedPrefsManager(requireContext()).checkToken()
//        if (check) {
//            findNavController().navigate(R.id.onboardingFragment)
//        }
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
                val emailEt = binding.emailET.text.toString()
                val passwordEt = binding.passwordET.text.toString()
                val nameEt = binding.nameET.text.toString()
                viewModel.signupUser(emailEt, passwordEt, nameEt, requireContext())
            }
        }
    }

}