package com.example.techtonic.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.techtonic.Fragments.Profile
import com.example.techtonic.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditProfile : AppCompatActivity() {
    private lateinit var fullnametv: EditText
    private lateinit var Save: Button
    private lateinit var Pic: Button
    private lateinit var emailTextView: EditText
    private lateinit var genderEditText: EditText
    private lateinit var phoneNumberTextView: EditText
    private lateinit var database: DatabaseReference
    private lateinit var userProfile: ImageView
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private var ProfileimageUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        database = FirebaseDatabase.getInstance().reference

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA
            ),
            1
        )
        fullnametv = findViewById(R.id.edtfullname)
        genderEditText = findViewById(R.id.edtgenders)
        emailTextView = findViewById(R.id.edtEmailAddress)
        phoneNumberTextView =findViewById(R.id.edtPhoneNumber)
        userProfile = findViewById(R.id.Profile)
        Save = findViewById(R.id.btn_save)
        Pic = findViewById(R.id.profileButton)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap: Bitmap? = result.data?.extras?.get("data") as? Bitmap
                bitmap?.let {
                    userProfile.setImageBitmap(it)
                    ProfileimageUri = saveImageAndGetUri(it)
                    if (ProfileimageUri != null) {
                        Log.d("HazardReport", "Image URI set successfully: $ProfileimageUri")
                    } else {
                        Log.e("HazardReport", "Failed to generate image URI")
                    }
                }
            }
        }

        Pic.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }
        Save.setOnClickListener {
            if (validateInput()) {
                submitEditProfile()
            }
        }

    }
    private fun validateInput(): Boolean {
        return when {
            fullnametv.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Fullname cannot be empty", Toast.LENGTH_SHORT).show()
                false
            }
            genderEditText.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Gender cannot be empty", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
    private fun saveImageAndGetUri(bitmap: Bitmap): Uri? {
        return try {
            val tempFile = File.createTempFile("profile_image", ".jpg", cacheDir)
            val outputStream = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            Log.d("ProfileImage", "Image saved to temporary file successfully: ${tempFile.absolutePath}")
            Uri.fromFile(tempFile)
        } catch (e: IOException) {
            Log.e("ProfileImage", "Failed to save image to temporary file", e)
            null
        }
    }
    private fun uploadImageToFirebase(uri: Uri, userId: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile/$userId.jpg")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { url ->
                    database.child("UserProfile/$userId/imageUrl").setValue(url.toString())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving image URL", Toast.LENGTH_SHORT).show()
                        }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun submitEditProfile() {
        val fullname = fullnametv.text.toString()
        val genderEditText = genderEditText.text.toString()
        val emailTextView = emailTextView.text.toString()
        val phoneNumberTextView = phoneNumberTextView.text.toString()

        val userId = "user_unique_id"
        val user = mapOf(
            "fullname" to fullname,
            "gender" to genderEditText,
            "email" to emailTextView,
            "phoneNumber" to phoneNumberTextView
        )


        database.child("UserProfile").child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (ProfileimageUri != null) {
                        uploadImageToFirebase(ProfileimageUri!!, userId)
                    } else {
                        Toast.makeText(this, "Profile submitted without an image", Toast.LENGTH_SHORT).show()
                    }
                    clearFields()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Profile()) // Use the correct container ID
                        .addToBackStack(null) // Optional: Add to back stack to allow returning to the report activity
                        .commit()
                } else {
                    Toast.makeText(this, "Error Editing Profile", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun clearFields() {
        fullnametv.text.clear()
        genderEditText.text.clear()
        phoneNumberTextView.text.clear()
        emailTextView.text.clear()
        userProfile.setImageDrawable(null)
    }
}