package com.example.spext
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.VideoView
import java.io.File
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.spext.R
import android.location.Geocoder
import android.location.Location
import android.util.Base64
import android.view.PixelCopy.Request
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.ByteArrayOutputStream
import java.util.Locale

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var nicknameEditText: EditText
    private lateinit var takePhotoButton: Button
    private lateinit var takeCountryAuto: Button
    private lateinit var saveButton: Button
    private lateinit var countryEditText: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        nicknameEditText = findViewById(R.id.nicknameEditText)
        takePhotoButton = findViewById(R.id.takePhotoButton)
        takeCountryAuto = findViewById(R.id.takeCountryAuto)
        profileImageView = findViewById(R.id.profileImageView)
        countryEditText = findViewById(R.id.countryEditText)
        saveButton = findViewById(R.id.saveButton)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        takePhotoButton.setOnClickListener {
            // Запуск интента для открытия камеры
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    100
                )
            } else {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, 1)
            }
        }

        takeCountryAuto.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
            } else {
                // Получение текущего местоположения при наличии разрешений
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        location?.let {
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                            val country = addresses?.get(0)?.countryName
                            // country - это страна проживания
                            countryEditText.setText(country)
                        }
                    }
            }
        }
        saveButton.setOnClickListener{
            val sharedPreferences = getSharedPreferences("ProfileData", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Сохранение никнейма
            val nickname = nicknameEditText.text.toString()
            editor.putString("nickname", nickname)

            // Сохранение страны проживания
            val country = countryEditText.text.toString()
            editor.putString("country", country)

            // Сохранение фото (в формате Base64)
            val baos = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.PNG, 50, baos)
            val byteArray = baos.toByteArray()
            val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
            editor.putString("profilePhoto", encodedImage)


            editor.apply()

            Toast.makeText(this, "Данные профиля сохранены", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            profileImageView.setImageBitmap(imageBitmap)
        }
    }
}