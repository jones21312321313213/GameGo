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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : Activity() {

    private lateinit var rootView: View
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_f3)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        rootView = findViewById(android.R.id.content)

        val etUsername = findViewById<EditText>(R.id.login_username_Id) // Change to username input
        val etPassword = findViewById<EditText>(R.id.login_pass_Id)

        intent?.let {
            it.getStringExtra("email")?.let { email ->
                etUsername.setText(email)  // Temporarily show email
            }
        }

        findViewById<TextView>(R.id.login_signup_Id).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.forgotPasswordText).setOnClickListener {
            startActivity(Intent(this, forget_password::class.java))
            finish()
        }

        findViewById<Button>(R.id.login_Id).setOnClickListener {
            val username = etUsername.text.toString().trim().replace("\\s".toRegex(), "")
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                showSnackbar("Username and password cannot be empty")
                return@setOnClickListener
            }

            loginUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {
        val database = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
        val reference = database.getReference("Users")

        reference.orderByChild("username").equalTo(username).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    // Retrieve the associated email for the username
                    val userData = dataSnapshot.children.first().value as? Map<*, *>
                    val email = userData?.get("email")?.toString() ?: ""

                    // Now use the email to log in the user via FirebaseAuth
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            val uid = auth.currentUser?.uid ?: return@addOnSuccessListener
                            val usernameFetched = userData?.get("username")?.toString() ?: ""

                            showSnackbar("Login successful 🎯")

                            // Navigate to the landing screen
                            startActivity(Intent(this, landingWIthFragmentActivity::class.java).apply {
                                putExtra("username", usernameFetched)
                                putExtra("email", email)
                                putExtra("uid", uid)
                            })
                            finish()
                        }
                        .addOnFailureListener {
                            showSnackbar("Login failed. Please check your credentials and try again.")
                            Log.e("AUTH", "Login error: ${it.message}")
                        }
                } else {
                    showSnackbar("User not found with that username")
                }
            }
            .addOnFailureListener {
                showSnackbar("Error fetching user data")
                Log.e("Firebase Error", "Error: ${it.message}")
            }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }
}
