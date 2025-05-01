package cit.edu.gamego

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.util.TypedValue
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream

class EditProfilePicture : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_picture)

        val name = findViewById<TextView>(R.id.edit_displayUserName_Id)
        val email = findViewById<TextView>(R.id.edit_displayEmail_Id)
        val newPass = findViewById<EditText>(R.id.newpass_Id)
        val confirmPass = findViewById<EditText>(R.id.edit_confirmpass_Id)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        //display current info
        if (currentUser != null) {
            val uid = currentUser.uid
            val dbRef = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
                .getReference("Users")
                .child(uid)

            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").getValue(String::class.java) ?: ""
                        val emailVal = snapshot.child("email").getValue(String::class.java) ?: ""

                        name.setText(username)
                        email.setText(emailVal)
                    } else {
                        Log.e("FIREBASE", "User data not found for UID: $uid")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FIREBASE", "DB read failed: ${error.message}")
                }
            })
        } else {
            Log.e("AUTH", "No user is logged in")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        findViewById<ImageView>(R.id.edit_pfp_back_Id).setOnClickListener {
            finish()
        }


        // updates
        findViewById<Button>(R.id.edit_save_Id).setOnClickListener {

            val newPassword = newPass.text.toString()
            val confirmPassword = confirmPass.text.toString()

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (currentUser != null) {
                val message = "Are you sure you want to change your password?"
                showSaveConfirmationPopUp(message, newPassword, currentUser)
            } else {
                Toast.makeText(this, "No authenticated user found", Toast.LENGTH_SHORT).show()
            }
        }



    }


    private fun showSaveConfirmationPopUp(
        message: String,
        newPassword: String,
        user: FirebaseUser
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.activity_logout_popout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val widthInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, resources.displayMetrics).toInt()
        val heightInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics).toInt()
        dialog.window?.setLayout(widthInPx, heightInPx)

        val btnYes = dialog.findViewById<Button>(R.id.yesLogout_id)
        val btnNo = dialog.findViewById<Button>(R.id.noLogout_id)
        val msg = dialog.findViewById<TextView>(R.id.message)

        msg.text = message
        dialog.show()

        btnYes.setOnClickListener {
            user.updatePassword(newPassword)
                .addOnSuccessListener {
                    // Update Realtime Database as well
                    val dbRef = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
                        .getReference("Users")
                        .child(user.uid)

                    dbRef.child("password").setValue(newPassword)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()

                            val resultIntent = Intent().apply {
                                putExtra("new_password", newPassword)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(this, "Password changed in Auth but failed in DB: ${error.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Failed to update password: ${error.message}", Toast.LENGTH_LONG).show()
                }

            dialog.dismiss()
        }



        btnNo.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun saveProfilePicToFirestore(uid: String?, imageUrl: String) {
        if (uid == null) return

        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(uid)

        userDocRef.set(mapOf("profile_picture" to imageUrl), SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile picture updated in Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save to Firestore: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


}
