package com.example.spext

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView


class GameActivity : AppCompatActivity() {
    private lateinit var textTextView: TextView
    private lateinit var inputEditText: EditText
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        textTextView = findViewById(R.id.textTextView)
        inputEditText = findViewById(R.id.inputEditText)

        val texts = resources.getStringArray(R.array.texts)
        val randomText = texts.random()
        textTextView.text = randomText

        startTime = System.currentTimeMillis()

        findViewById<Button>(R.id.finishButton).setOnClickListener {
            endTime = System.currentTimeMillis()
            val elapsedTime = ((endTime - startTime) / 1000)
            val wordsPerMinute = calculateWordsPerMinute(inputEditText.text.toString(), elapsedTime).toString()
            val errorCount = calculateErrorCount(randomText, inputEditText.text.toString()).toString()

            val sharedPreferences = getSharedPreferences("ProfileData", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("time", elapsedTime.toString())
            editor.putString("WPM", wordsPerMinute)
            editor.putString("errorCount", errorCount)

            editor.apply()
            val intent = Intent(this, ShowResultsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun calculateWordsPerMinute(inputText: String, elapsedTime: Long): Int {
            val words = inputText.trim().split(" ").size
            val minutes = elapsedTime / 60.0
            return (words / minutes).toInt()
    }

    private fun calculateErrorCount(text: String, inputText: String): Int {
        val correctWords = text.trim().split(" ")
        val inputWords = inputText.trim().split(" ")
        var errorCount = 0

        for (i in correctWords.indices) {
            if (i < inputWords.size && correctWords[i] != inputWords[i]) {
                errorCount++
            }
        }

        return errorCount
    }
}