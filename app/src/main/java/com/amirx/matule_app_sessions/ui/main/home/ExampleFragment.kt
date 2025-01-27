package com.amirx.matule_app_sessions.ui.main.home

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.databinding.FragmentExampleBinding
import com.amirx.matule_app_sessions.ui.base.BaseFragment
import java.io.File


class ExampleFragment : Fragment() {

    // Переменные для хранения путей и результата
    private var cameraImageUri: Uri? = null

    private val binding: FragmentExampleBinding by lazy { FragmentExampleBinding.inflate(layoutInflater) }
    // Photo Picker для галереи
    private val pickImageFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                // URI выбранного изображения
                showImage(uri)
            } else {
                Toast.makeText(requireContext(), "Изображение не выбрано", Toast.LENGTH_SHORT).show()
            }
        }

    // Камера
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && cameraImageUri != null) {
                // URI сохраненного изображения с камеры
                showImage(cameraImageUri!!)
            } else {
                Toast.makeText(requireContext(), "Снимок не сделан", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = binding.root

        // Кнопка для выбора изображения из галереи
//       binding.image.setOnClickListener {
//            pickImageFromGallery()
//        }

//        // Кнопка для запуска камеры
//        binding.image.setOnClickListener {
//            takePicture()
//        }

        binding.image.setOnClickListener {
            showCustomDialog()
        }

        return view
    }
    private fun pickImageFromGallery() {
        pickImageFromGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun takePicture() {
        val imageFile = File.createTempFile(
            "IMG_${System.currentTimeMillis()}",
            ".jpg",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        cameraImageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
        takePictureLauncher.launch(cameraImageUri)
    }

    fun showCustomDialog() {
        // Создаем диалог
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog, null)
        dialogBuilder.setView(dialogView)

        // Настраиваем элементы диалога
        val dialog = dialogBuilder.create()

        val titleTextView = dialogView.findViewById<TextView>(R.id.tvConfirmTitle)
        val messageTextView = dialogView.findViewById<TextView>(R.id.tvConfirmMessage)
        val positiveButton = dialogView.findViewById<Button>(R.id.btnConfirm)
        val negativeButton = dialogView.findViewById<Button>(R.id.btnCancel)

        titleTextView.text = "Привет!"
        messageTextView.text = "Это кастомный диалог с твоим дизайном."

        positiveButton.setOnClickListener {
            Toast.makeText(requireContext(), "Нажали ОК", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        negativeButton.setOnClickListener {
            Toast.makeText(requireContext(), "Нажали Отмена", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        // Показываем диалог
        dialog.show()
    }

    // Показ изображения (пример)
    private fun showImage(uri: Uri) {
        binding.image.setImageURI(uri)
    }
}