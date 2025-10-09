package com.example.healthfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private val VALID_INPUT_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginLayout = findViewById<TextInputLayout>(R.id.inputLogin)
        val passwordLayout = findViewById<TextInputLayout>(R.id.inputPassword)
        val confirmPasswordLayout = findViewById<TextInputLayout>(R.id.confirmPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        buttonRegister.setOnClickListener { view ->
            val login = loginLayout.editText?.text.toString().trim()
            val password = passwordLayout.editText?.text.toString().trim()
            val confirmPassword = confirmPasswordLayout.editText?.text.toString().trim()

            loginLayout.error = null
            passwordLayout.error = null
            confirmPasswordLayout.error = null

            when {
                login.isBlank() -> {
                    loginLayout.error = getString(R.string.login_warning)
                    return@setOnClickListener
                }
                password.isBlank() -> {
                    passwordLayout.error = getString(R.string.password_warning)
                    return@setOnClickListener
                }
                confirmPassword.isBlank() -> {
                    confirmPasswordLayout.error = getString(R.string.password_warning)
                    return@setOnClickListener
                }
                !VALID_INPUT_PATTERN.matcher(login).matches() -> {
                    loginLayout.error = getString(R.string.login_pattern)
                    return@setOnClickListener
                }
                !VALID_INPUT_PATTERN.matcher(password).matches() -> {
                    passwordLayout.error = getString(R.string.login_pattern)
                    return@setOnClickListener
                }
                password != confirmPassword -> {
                    confirmPasswordLayout.error = getString(R.string.password_fail)
                    return@setOnClickListener
                }
                else -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}