package com.amirx.matule_app_sessions.domain.usecase

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.databinding.DialogBinding

class CustomConfirmDialog(
    context: Context,
    private val title: String,
    private val message: String,
    private val onConfirm: () -> Unit,
    private val onCancel: (() -> Unit)? = null
) {
    private val dialog: Dialog = Dialog(context, R.style.CustomDialog)
    private lateinit var binding: DialogBinding

    init {
        binding = DialogBinding.inflate(LayoutInflater.from(context))
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
            tvConfirmTitle.text = title
            tvConfirmMessage.text = message

            btnConfirm.setOnClickListener {
                dialog.dismiss()
                onConfirm()
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
                onCancel?.invoke()
            }
        }
    }

    fun show() {
        dialog.show()
    }
}