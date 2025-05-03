package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import cit.edu.gamego.data.Users
import cit.edu.gamego.databinding.RegisterF3Binding
import cit.edu.gamego.extensions.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : Activity() {
    private lateinit var binding: RegisterF3Binding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterF3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
        reference = database.getReference("Users")

        binding.registerSignupId.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.registerBtnId.setOnClickListener {
            val username = binding.registerUsernameId.text.toString().trim()
            val email = binding.registerEmailId.text.toString().trim()
            val password = binding.registerPassId.text.toString().trim()
            val cpass = binding.registerConfirmpassId.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || cpass.isEmpty()) {
                toast("Please complete all fields")
                return@setOnClickListener
            }

            if (password != cpass) {
                toast("Passwords do not match")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            val userData = Users(
                                username = username,
                                email = email,
                                favorites = listOf(""),
                                profilePicUrl = " "
                            )

                            reference.child(uid).setValue(userData)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        toast("User registered successfully")
                                        clearFields()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        toast("Failed to save user data")
                                    }
                                }
                        }
                    } else {
                        toast("Registration failed: ${authTask.exception?.message}")
                    }
                }
        }
    }

    private fun clearFields() {
        binding.registerUsernameId.setText("")
        binding.registerEmailId.setText("")
        binding.registerPassId.setText("")
        binding.registerConfirmpassId.setText("")
    }
}
