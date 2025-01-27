package com.amirx.matule_app_sessions.ui.authorization.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.databinding.FragmentOtpBinding
import com.amirx.matule_app_sessions.domain.usecase.CustomDialogPassword
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import kotlinx.coroutines.launch

class OtpFragment : BaseFragment() {
    private val binding: FragmentOtpBinding by lazy { FragmentOtpBinding.inflate(layoutInflater) }
//    private val correctOtp = "000000" // Замените на ваш правильный OTP-код

    private val viewModel: OtpViewModel by viewModels(factoryProducer = {
        OtpViewModelFactory()
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimer()
        setupOtpEditTexts()
    }

    private fun startTimer() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timerTxt.text = "${millisUntilFinished / 1000}"
                binding.sendAgainTxt.visibility = View.GONE
            }

            override fun onFinish() {
                binding.timerTxt.text = "0:00"
                binding.sendAgainTxt.visibility = View.VISIBLE
            }
        }.start()
    }

    override fun applyClick() {
        super.applyClick()
        binding.sendAgainTxt.setOnClickListener {
            startTimer()
            lifecycleScope.launch {
                checkOtpAndShowDialog()
            }
        }
        binding.backBt.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupOtpEditTexts() {
        binding.otp6.addTextChangedListener {
            lifecycleScope.launch {
                if (it?.length == 1) {
                    checkOtpAndShowDialog()
                }
            }

        }

        binding.otp1.addTextChangedListener { if (it?.length == 1) binding.otp2.requestFocus() }
        binding.otp2.addTextChangedListener { if (it?.length == 1) binding.otp3.requestFocus() }
        binding.otp3.addTextChangedListener { if (it?.length == 1) binding.otp4.requestFocus() }
        binding.otp4.addTextChangedListener { if (it?.length == 1) binding.otp5.requestFocus() }
        binding.otp5.addTextChangedListener { if (it?.length == 1) binding.otp6.requestFocus() }
    }

    private suspend fun checkOtpAndShowDialog() {
        val enteredOtp =
            "${binding.otp1.text}${binding.otp2.text}${binding.otp3.text}${binding.otp4.text}${binding.otp5.text}${binding.otp6.text}"

        viewModel.checkOtp("puknitov@gmail.com", enteredOtp)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Success -> {
                    setOtpBackgrounds(R.drawable.edit_text_search_background)
                    showCorrectDialog()
                }

                is ResponseState.Error -> {
                    setOtpBackgrounds(R.drawable.incorrect_text_search_background)

                }

                is ResponseState.Loading -> {

                }

                else -> {}

            }
        }
    }

    private fun setOtpBackgrounds(backgroundResId: Int) {
        val background = ContextCompat.getDrawable(requireContext(), backgroundResId)
        binding.otp1.background = background
        binding.otp2.background = background
        binding.otp3.background = background
        binding.otp4.background = background
        binding.otp5.background = background
        binding.otp6.background = background
    }

    private fun showCorrectDialog() {
        CustomDialogPassword(
            requireContext(),
            "Придумайте новый пароль",
            "Ниже написан сгенерированный пароль",
            {
                findNavController().navigate(R.id.homeFragment)
            }).show()
    }
}