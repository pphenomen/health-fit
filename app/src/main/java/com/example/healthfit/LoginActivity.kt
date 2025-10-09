package com.example.healthfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class LoginActivity : BaseActivity() {

    private val VALID_INPUT_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginLayout = findViewById<TextInputLayout>(R.id.inputLogin)
        val passwordLayout = findViewById<TextInputLayout>(R.id.inputPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val linkRegister = findViewById<TextView>(R.id.registerLink)

        // обработка нажатия "зарегистрироваться"
        linkRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        // обработка нажатия "войти"
        buttonLogin.setOnClickListener {
            val login = loginLayout.editText?.text.toString().trim()
            val password = passwordLayout.editText?.text.toString().trim()

            loginLayout.error = null
            passwordLayout.error = null

            when {
                login.isBlank() -> loginLayout.error = getString(R.string.enter_login)
                password.isBlank() -> passwordLayout.error = getString(R.string.enter_password)
                !VALID_INPUT_PATTERN.matcher(login).matches() -> loginLayout.error = getString(R.string.login_pattern)
                !VALID_INPUT_PATTERN.matcher(password).matches() -> passwordLayout.error = getString(R.string.login_pattern)
                else -> {
                    // успешный вход
                    startActivity(Intent(this, DiaryActivity::class.java))
                    finish()
                    Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
        }
    }
}