package com.example.healthfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin: Button = findViewById(R.id.buttonLogin)
        // временный редирект сразу в дневник
        buttonLogin.setOnClickListener {
            val intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
