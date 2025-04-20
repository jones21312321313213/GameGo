package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : Activity() {

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_f3)

        rootView = findViewById(android.R.id.content) // root view for Snackbar
        rootView.post {
            Snackbar.make(rootView, "Test Snackbar", Snackbar.LENGTH_LONG).show()
        }
        val et_username = findViewById<EditText>(R.id.login_username_Id)
        val et_password = findViewById<EditText>(R.id.login_pass_Id)

        intent?.let {
            it.getStringExtra("username")?.let { username ->
                et_username.setText(username)
            }
            it.getStringExtra("password")?.let { password ->
                et_password.setText(password)
            }
        }

        val button_register = findViewById<TextView>(R.id.login_signup_Id)
        button_register.setOnClickListener {
            Log.e("CSIT 284", "absolute cinema")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnMain = findViewById<Button>(R.id.login_Id)
        btnMain.setOnClickListener {
            if (et_username.text.toString().isNullOrEmpty() || et_password.text.toString().isNullOrEmpty()) {
                showSnackbar("Username and password is empty")
                return@setOnClickListener
            }
            loginUser(et_username.text.toString(), et_password.text.toString())
        }
    }

    private fun loginUser(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            showSnackbar("Enter username and password")
            return
        }

        val database = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
        val reference = database.getReference("Users")

        reference.child(username).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val dbPassword = dataSnapshot.child("password").value.toString()
                val email = dataSnapshot.child("email").value.toString()

                if (password == dbPassword) {
                    showSnackbar("Login successful")
                    startActivity(
                        Intent(this, landingWIthFragmentActivity::class.java).apply {
                            putExtra("username", username)
                            putExtra("email", email)
                        }
                    )
                    finish()
                } else {
                    showSnackbar("Incorrect password")
                }
            } else {
                showSnackbar("User not found")
            }
        }.addOnFailureListener {
            showSnackbar("Login failed. Try again.")
            Log.e("Firebase Error", "Error: ${it.message}")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }
}
