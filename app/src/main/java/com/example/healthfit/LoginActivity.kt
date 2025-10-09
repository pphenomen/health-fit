package com.example.healthfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private val VALID_INPUT_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$")

    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = getSharedPreferences("prefs", MODE_PRIVATE)
        val isDark = pref.getBoolean("dark_theme", false)
        setTheme(if (isDark) R.style.Theme_HealthFit_Dark else R.style.Theme_HealthFit)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginLayout = findViewById<TextInputLayout>(R.id.inputLogin)
        val passwordLayout = findViewById<TextInputLayout>(R.id.inputPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val linkRegister = findViewById<TextView>(R.id.registerLink)

        linkRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        buttonLogin.setOnClickListener {
            val login = loginLayout.editText?.text.toString().trim()
            val password = passwordLayout.editText?.text.toString().trim()

            loginLayout.error = null
            passwordLayout.error = null

            when {
                login.isBlank() -> {
                    loginLayout.error = getString(R.string.enter_login)
                    return@setOnClickListener
                }
                password.isBlank() -> {
                    passwordLayout.error = getString(R.string.enter_password)
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
                else -> {
                    startActivity(Intent(this, DiaryActivity::class.java))
                    finish()
                    Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}