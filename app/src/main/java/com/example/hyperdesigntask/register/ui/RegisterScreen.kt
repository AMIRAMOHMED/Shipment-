package com.example.hyperdesigntask.register.ui
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hyperdesigntask.R
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
    class RegisterScreen : AppCompatActivity() {
        private val registerViewModel: RegisterViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_main)

            // Apply edge-to-edge layout
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            val fakeFile = createFakeFile()

            findViewById<Button>(R.id.uploadButton).setOnClickListener {
                val requestFile = RequestBody.create(MultipartBody.FORM, fakeFile)
                val filePart = MultipartBody.Part.createFormData("file", fakeFile.name, requestFile)

                val request = RegisterRequest(
                    name = "name",
                    email = "email",
                    phone = "phone",
                    password = "password",
                    country_id = "country_id",
                    type = "employee",
                    file =filePart

                )
                // Trigger fake registration
                registerViewModel.registerUser(request)
            }



        }
    // Function to create a fake file
    private fun createFakeFile(): File {
        // Creating a temporary file for testing
        val tempFile = File(filesDir, "fakeFile.txt")
        FileOutputStream(tempFile).use {
            it.write("This is a fake file for testing.".toByteArray())
        }
        return tempFile
    }
    }