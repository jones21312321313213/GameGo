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
import com.google.firebase.auth.FirebaseAuth
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

        reference.orderByChild("username").equalTo(username).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (userSnapshot in dataSnapshot.children) {
                    val userData = userSnapshot.value as? HashMap<*, *>
                    val dbPassword = userData?.get("password")?.toString() ?: ""
                    val email = userData?.get("email")?.toString() ?: ""
                    val dbUsername = userData?.get("username")?.toString() ?: ""
                    val uid = userSnapshot.key

                    if (password == dbPassword) {
                        // 🔥 Firebase Auth login with email + password
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                showSnackbar("Login successful 🎯")
                                startActivity(
                                    Intent(this, landingWIthFragmentActivity::class.java).apply {
                                        putExtra("username", dbUsername)
                                        putExtra("email", email)
                                        putExtra("uid", uid)
                                    }
                                )
                                finish()
                            }
                            .addOnFailureListener {
                                Log.e("AUTH", "FirebaseAuth login failed: ${it.message}")
                                showSnackbar("Login failed at auth level: ${it.message}")
                            }
                    } else {
                        showSnackbar("Incorrect password")
                    }
                    return@addOnSuccessListener
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
