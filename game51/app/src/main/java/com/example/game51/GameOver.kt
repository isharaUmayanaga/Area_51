package com.example.game51

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {
    private lateinit var displayPoint: TextView
    private lateinit var displayHighScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)
        val points = intent.getIntExtra("point", 0)
        val highScore = intent.getIntExtra("highScore", 0) // Retrieving high score

        displayPoint = findViewById(R.id.displayPoint)
        displayHighScore = findViewById(R.id.displayHighScore) // Initializing high score TextView

        displayPoint.text = points.toString()
        displayHighScore.text = highScore.toString() // Displaying high score
    }

    fun restart(v: View) {
        val intent = Intent(this@GameOver, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun cancel(v: View) {
        finish()
    }
}
