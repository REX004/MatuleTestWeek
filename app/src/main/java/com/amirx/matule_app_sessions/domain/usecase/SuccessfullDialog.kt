package com.amirx.matule_app_sessions.domain.usecase

import android.app.Dialog
import android.content.Context
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.databinding.DialogPasswordBinding
import com.amirx.matule_app_sessions.databinding.DialogSuccessBinding
import com.ibm.icu.text.Transliterator
import kotlin.random.Random

class SuccessfullDialog(
    context: Context,
    private val message: String,
    private val onConfirm: () -> Unit,
) {
    private val dialog: Dialog = Dialog(context, R.style.CustomDialog)
    private lateinit var binding: DialogSuccessBinding

    init {
        binding = DialogSuccessBinding.inflate(LayoutInflater.from(context))
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            setCancelable(true)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
        setupDialog()
    }

    private fun setupDialog() {
        binding.apply {
            tvConfirmMessage.text = message

            generateBt.setOnClickListener {
                onConfirm()
                dialog.dismiss()
            }
        }
    }

    fun show() {
        dialog.show()
    }
}