package com.example.healthfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class RegisterActivity : BaseActivity() {

    private val VALID_INPUT_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginLayout = findViewById<TextInputLayout>(R.id.inputLogin)
        val passwordLayout = findViewById<TextInputLayout>(R.id.inputPassword)
        val confirmPasswordLayout = findViewById<TextInputLayout>(R.id.confirmPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        // обработка нажатия "зарегистрироваться"
        buttonRegister.setOnClickListener {
            val login = loginLayout.editText?.text.toString().trim()
            val password = passwordLayout.editText?.text.toString().trim()
            val confirmPassword = confirmPasswordLayout.editText?.text.toString().trim()

            loginLayout.error = null
            passwordLayout.error = null
            confirmPasswordLayout.error = null

            when {
                login.isBlank() -> loginLayout.error = getString(R.string.enter_login)
                password.isBlank() -> passwordLayout.error = getString(R.string.enter_password)
                confirmPassword.isBlank() -> confirmPasswordLayout.error = getString(R.string.enter_password)
                !VALID_INPUT_PATTERN.matcher(login).matches() -> loginLayout.error = getString(R.string.login_pattern)
                !VALID_INPUT_PATTERN.matcher(password).matches() -> passwordLayout.error = getString(R.string.login_pattern)
                password != confirmPassword -> confirmPasswordLayout.error = getString(R.string.password_fail)
                else -> {
                    // успешная регистрация
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
        }

        // обработка нажатия "войти"
        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}