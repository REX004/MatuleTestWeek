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
import com.ibm.icu.text.Transliterator
import kotlin.random.Random

class CustomDialogPassword(
    context: Context,
    private val title: String,
    private val message: String,
    private val onConfirm: () -> Unit,
    private val onCancel: (() -> Unit)? = null
) {
    private val dialog: Dialog = Dialog(context, R.style.CustomDialog)
    private lateinit var binding: DialogPasswordBinding

    init {
        binding = DialogPasswordBinding.inflate(LayoutInflater.from(context))
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

            // Обработчик изменений текста
            binding.phraseEt.addTextChangedListener { text ->
                val phrase = text.toString()
                val transformedText = transformText(phrase)
                binding.tvNewPassword.text = transformedText
            }

            nextBt.setOnClickListener {
                onConfirm()
                dialog.dismiss()
            }

            generateBt.setOnClickListener { view ->
                // Копирование текста в буфер обмена
                val clipboard =
                    view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val textToCopy = binding.tvNewPassword.text.toString()
                val clip = ClipData.newPlainText("GeneratedText", textToCopy)
                clipboard.setPrimaryClip(clip)

                // Переход на другой фрагмент
                onConfirm()

                // Закрытие диалога
                dialog.dismiss()
            }
        }
    }

    // Функция для обработки текста
    private fun transformText(input: String): String {
        // Транслитерация кириллицы в латиницу
        val cyrillicToLatinTrans = Transliterator.getInstance("Russian-Latin/BGN")
        val transliterated = cyrillicToLatinTrans.transliterate(input)

        // Обработка текста после транслитерации (замены и случайный регистр)
        val replacementMap = mapOf(
            'o' to '0',  // латинская "o"
            'e' to '3',
            'a' to '@',
            's' to '$',
            't' to '7',
            'i' to '|',
            'b' to '6',
            'y' to '9',
            'c' to '('
        )

        // Построение нового текста
        return transliterated.map { char ->
            // Заменить символ, если он есть в словаре
            val replacedChar = replacementMap[char.lowercaseChar()] ?: char

            // Применить случайный регистр
            if (Random.nextBoolean()) replacedChar.uppercaseChar() else replacedChar.lowercaseChar()
        }.joinToString("")
    }

    fun show() {
        dialog.show()
    }
}