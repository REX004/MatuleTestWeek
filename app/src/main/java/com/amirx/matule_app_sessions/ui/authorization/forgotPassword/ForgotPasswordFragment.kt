package com.amirx.matule_app_sessions.ui.authorization.forgotPassword

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
import com.amirx.matule_app_sessions.databinding.DialogAuthorizationBinding
import com.amirx.matule_app_sessions.databinding.DialogBinding
import com.amirx.matule_app_sessions.databinding.FragmentForgotPasswordBinding
import com.amirx.matule_app_sessions.databinding.FragmentLoginBinding
import com.amirx.matule_app_sessions.domain.usecase.CustomConfirmDialog
import com.amirx.matule_app_sessions.domain.usecase.DialogAuthorization
import kotlinx.coroutines.launch


class ForgotPasswordFragment : Fragment() {

    private val binding: FragmentForgotPasswordBinding by lazy {
        FragmentForgotPasswordBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: ForgotPasswordViewModel by viewModels(factoryProducer = {
        ForgotPasswordViewModelProvider()
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private val dialogBinding: DialogAuthorizationBinding by lazy {
        DialogAuthorizationBinding.inflate(
            layoutInflater
        )
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
                    findNavController().navigate(R.id.otpFragment)
                }

                is LoginState.Loading -> {
                    binding.mainContainer.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        val dialog = DialogAuthorization(requireContext(), "Проверьте Ваш Email", message, {
            null
        }).show()

    }

    private fun applyClick() {
        binding.loginBt.setOnClickListener {
            lifecycleScope.launch {
                val emailEt = binding.emailET.text.toString()
                viewModel.loginUser(emailEt, requireContext())
            }
            showErrorDialog("Мы отправили код восстановления пароля на вашу электронную почту.")
            findNavController().navigate(R.id.otpFragment)
        }
        dialogBinding.root.setOnClickListener {
            findNavController().navigate(R.id.otpFragment)
        }
    }

}