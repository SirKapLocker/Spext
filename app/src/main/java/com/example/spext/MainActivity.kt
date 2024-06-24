package com.example.spext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.spext.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.createProfileButton.setOnClickListener{
            startActivity(Intent(this, CreateProfileActivity::class.java))
        }

        binding.startGameButton.setOnClickListener{
            startActivity(Intent(this, GameActivity::class.java))
        }

        binding.showResultButton.setOnClickListener{
            startActivity(Intent(this, ShowResultsActivity::class.java))
        }

    }
}