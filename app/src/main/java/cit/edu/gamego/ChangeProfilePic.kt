package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cit.edu.gamego.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ChangeProfilePic : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var chooseImage: Button
    private lateinit var uploadImage: Button
    private var imageUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profilepic)

        imageView = findViewById(R.id.image_view)
        chooseImage = findViewById(R.id.choose_image)
        uploadImage = findViewById(R.id.upload_image)

        val back = findViewById<ImageView>(R.id.back_rp)


        chooseImage.setOnClickListener {
            pickImageFromGallery()
        }

        uploadImage.setOnClickListener {
            imageUri?.let {
                uploadToImgBB(it)
            } ?: Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }

        back.setOnClickListener{
            startActivity(
                Intent(this,settingsFragment::class.java)
            )
            finish()
        }

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadToImgBB(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val imageBytes = inputStream?.readBytes() ?: return
        val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        val apiKey = BuildConfig.IMGBB_API_KEY
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("key", apiKey)
            .add("image", base64Image)
            .build()

        val request = Request.Builder()
            .url("https://api.imgbb.com/1/upload")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ChangeProfilePic, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val json = JSONObject(responseBody ?: "")
                val imageUrl = json.getJSONObject("data").getString("url")
                runOnUiThread {
                    saveImageUrlToDatabase(imageUrl)
                }
            }
        })
    }

    private fun saveImageUrlToDatabase(url: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance("https://gamego-5912c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(userId)
            databaseReference.child("profilePicUrl").setValue(url)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile picture URL saved to database", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to save URL: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
}
