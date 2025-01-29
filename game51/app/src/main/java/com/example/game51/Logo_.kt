package com.example.game51

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class Logo_ : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)
    }

    fun logo(v: View) {
        val intent = Intent(this@Logo_, StartUp::class.java)
        startActivity(intent)
        finish()
    }
}
