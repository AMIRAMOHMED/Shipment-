package com.example.hyperdesigntask.auth.ui
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.hyperdesigntask.auth.viewmodel.LoginViewModel
import com.example.hyperdesigntask.data.model.LoginRequest
import com.example.hyperdesigntask.databinding.ActivityLoginBinding
import com.example.hyperdesigntask.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set up ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.login.setOnClickListener {


            if (validateFields()) {

                val request = LoginRequest(
                    phone = binding.phone.text.toString().trim(),
                    password = binding.password.text.toString().trim(),
                    token = "fcm-token"
                )
                loginViewModel.loginUer(request)
                showToast("Login triggered!")
                observeLoginState()
            }
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginState.collect { state ->
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

    private fun validateFields(): Boolean {
        var isValid = true


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


        return isValid
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}