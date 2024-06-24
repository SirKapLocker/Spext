package com.example.spext

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ShowResultsActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var nicknameTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var WPMTextView: TextView
    private lateinit var errorCountTextView: TextView
    private lateinit var backToMenuButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_results)

        profileImageView = findViewById(R.id.profileImageView)
        nicknameTextView = findViewById(R.id.nicknameTextView)
        countryTextView = findViewById(R.id.countryTextView)
        timeTextView = findViewById(R.id.timeTextView)
        WPMTextView = findViewById(R.id.WPMTextView)
        errorCountTextView = findViewById(R.id.errorCountTextView)
        backToMenuButton = findViewById(R.id.backToMenu)

        val sharedPreferences = getSharedPreferences("ProfileData", Context.MODE_PRIVATE)

        // Получение сохраненных данных из SharedPreferences
        val nickname = sharedPreferences.getString("nickname", "")
        nicknameTextView.text = "Никнейм: $nickname"

        val country = sharedPreferences.getString("country", "")
        countryTextView.text = "Страна: $country"

        val time = sharedPreferences.getString("time", "")
        timeTextView.text = "Время: $time"

        val WPM = sharedPreferences.getString("WPM", "")
        WPMTextView.text = "Слов в минуту: $WPM"

        val errors = sharedPreferences.getString("errorCount", "")
        errorCountTextView.text = "Ошибок: $errors"

        val encodedImage = sharedPreferences.getString("profilePhoto", "")
        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        profileImageView.setImageBitmap(decodedBitmap)

        backToMenuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}
