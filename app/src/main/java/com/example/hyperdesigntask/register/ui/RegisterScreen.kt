package com.example.hyperdesigntask.register.ui
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.databinding.ActivityMainBinding
import com.example.hyperdesigntask.register.viewmodel.RegisterViewModel
import com.example.hyperdesigntask.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


@AndroidEntryPoint
class RegisterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedImageUri: Uri? = null

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
        // Gallery Picker
        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    selectedImageUri = uri
                    Glide.with(this).load(uri).into(binding.profilePhoto)
                }
            }
        // Set up upload button click listener
        binding.uploadButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
//        binding.singUp.setOnClickListener {
//            registerViewModel.refreshToken()
//
//        }

//         Set up sign-up button click listener
        binding.singUp.setOnClickListener {
            if (validateFields()) {
                val imageFile = createFileFromUri(selectedImageUri!!)
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                val filePart = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
                val request = RegisterRequest(
                    name = binding.name.text.toString().trim(),
                    email = binding.email.text.toString().trim(),
                    phone = binding.phone.text.toString().trim(),
                    password = binding.password.text.toString().trim(),
                    country_id = "9",
                    type = "employee",
                    file = filePart
                )

                // Trigger registration
                registerViewModel.registerUser(request)
                showToast("Registration triggered!")
                observeRegisterState()

            }
        }
    }

    private fun observeRegisterState() {
        lifecycleScope.launchWhenStarted {
            registerViewModel.registerState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val user = state.data.user
                        showSnackbar("Registration successful for ${user?.name ?: "unknown"}")
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showSnackbar(state.message)
                    }
                }
            }
        }
    }

    private fun createFileFromUri(uri: Uri): File {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "uploaded_image_${System.currentTimeMillis()}.png")
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    }


    // Function to validate form fields
    private fun validateFields(): Boolean {
        var isValid = true

        if (binding.name.text.isNullOrEmpty()) {
            binding.name.error = "Please enter your name"
            isValid = false
        } else {
            binding.name.error = null
        }
        if (binding.email.text.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.email.text).matches()) {
            binding.email.error = "Please enter a valid email"
            isValid = false
        } else {
            binding.email.error = null
        }
        val phoneRegex = Regex("^01[0-2][0-9]{8}\$")
        if (binding.phone.text.isNullOrEmpty() || !phoneRegex.matches(binding.phone.text.toString())) {
            binding.phone.error = "Please enter a valid phone number"
            isValid = false
        } else {
            binding.phone.error = null
        }
        if (binding.password.text.isNullOrEmpty()) {
            binding.password.error = "Please enter your password"
            isValid = false
        } else {
            binding.password.error = null
        }
        if (binding.repeatedPassword.text.isNullOrEmpty() || binding.repeatedPassword.text.toString() != binding.password.text.toString()) {
            binding.repeatedPassword.error = "Passwords do not match"
            isValid = false
        } else {
            binding.repeatedPassword.error = null
        }

        return isValid
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
