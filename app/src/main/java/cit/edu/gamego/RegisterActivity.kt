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
import com.rejowan.cutetoast.CuteToast

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
        database = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)//FIREBASE_DB_URL is foudn in the local properties and is defined in the grade(module:app)
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

                CuteToast.ct(this, "Please complete all fields", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                return@setOnClickListener
            }

            if (password != cpass) {
                CuteToast.ct(this, "Passwords do not match", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
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
                                profilePicUrl = "https://i.ibb.co/YTL00wCJ/34adab4b2153.jpg" // default profilepic
                            )
                            reference.child(uid).setValue(userData)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        CuteToast.ct(this, "User registered successfully", CuteToast.LENGTH_SHORT, CuteToast.HAPPY, true).show();
                                        clearFields()
                                        startActivity(
                                            Intent(this, LoginActivity::class.java).apply {
                                                putExtra("username",username)
                                                putExtra("password",password)
                                            }
                                        )
                                        finish()
                                    } else {
                                        CuteToast.ct(this, "Failed to save user data", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
                                    }
                                }
                        }
                    } else {
                        CuteToast.ct(this, "Registration failed: ${authTask.exception?.message}", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
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
