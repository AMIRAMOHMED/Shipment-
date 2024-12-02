package com.example.hyperdesigntask.register.ui
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hyperdesigntask.R
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.databinding.ActivityMainBinding
import com.example.hyperdesigntask.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream



@AndroidEntryPoint
class RegisterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set up ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up sign-up button click listener
        binding.singUp.setOnClickListener {
            if (validateFields()) {
                val fakeFile = createFakeFile()
                val requestFile = RequestBody.create(MultipartBody.FORM, fakeFile)
                val filePart = MultipartBody.Part.createFormData("file", fakeFile.name, requestFile)

                val request = RegisterRequest(
                    name = binding.name.text.toString().trim(),
                    email = binding.email.text.toString().trim(),
                    phone = binding.phone.text.toString().trim(),
                    password = binding.password.text.toString().trim(),
                    country_id = "9", // Replace with actual value
                    type = "employee",
                    file = filePart
                )

                // Trigger registration
                registerViewModel.registerUser(request)
                Toast.makeText(this, "Registration triggered!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to validate form fields
    private fun validateFields(): Boolean {
        var isValid = true

        // Validate name
        if (binding.name.text.isNullOrEmpty()) {
            binding.name.error = "Please enter your name"
            isValid = false
        } else {
            binding.name.error = null
        }

        // Validate email
        if (binding.email.text.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.email.text).matches()) {
            binding.email.error = "Please enter a valid email"
            isValid = false
        } else {
            binding.email.error = null
        }

        // Validate phone
        val phoneRegex = Regex("^01[0-2][0-9]{8}\$")
        if (binding.phone.text.isNullOrEmpty() || !phoneRegex.matches(binding.phone.text.toString())) {
            binding.phone.error = "Please enter a valid phone number"
            isValid = false
        } else {
            binding.phone.error = null
        }

        // Validate password
        if (binding.password.text.isNullOrEmpty()) {
            binding.password.error = "Please enter your password"
            isValid = false
        } else {
            binding.password.error = null
        }

        // Validate repeated password
        if (binding.repeatedPassword.text.isNullOrEmpty() || binding.repeatedPassword.text.toString() != binding.password.text.toString()) {
            binding.repeatedPassword.error = "Passwords do not match"
            isValid = false
        } else {
            binding.repeatedPassword.error = null
        }

        return isValid
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
