package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import cit.edu.gamego.extensions.toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rejowan.cutetoast.CuteToast
import com.royrodriguez.transitionbutton.TransitionButton
import android.graphics.Color
import android.widget.ScrollView

class LoginActivity : Activity() {
    private lateinit var rootView: View
    private lateinit var auth: FirebaseAuth
    private lateinit var transitionButton: TransitionButton // Declare TransitionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_f3)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        rootView = findViewById(android.R.id.content)


        transitionButton = findViewById(R.id.login_Id) // Initialize the TransitionButton

        transitionButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF0000")) // Red tint
        transitionButton.setTextColor(Color.parseColor("#FFFFFF")) // White text

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

        transitionButton.setOnClickListener {
            val username = etUsername.text.toString().trim().replace("\\s".toRegex(), "")
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                CuteToast.ct(this, "Username and password cannot be empty", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                return@setOnClickListener
            }

            // Start the loading animation when the button is clicked
            transitionButton.startAnimation()

            loginUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {
        // Resize the button to match the root layout



        val database = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
        val reference = database.getReference("Users")

        reference.orderByChild("username").equalTo(username).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    // Retrieve the associated email for the username
                    val userData = dataSnapshot.children.first().value as? Map<*, *>
                    val email = userData?.get("email")?.toString() ?: ""


                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            val uid = auth.currentUser?.uid ?: return@addOnSuccessListener
                            val usernameFetched = userData?.get("username")?.toString() ?: ""

                            val rootLayout = findViewById<ScrollView>(R.id.loginRoot)
                            val layoutParams = transitionButton.layoutParams
                            layoutParams.width = rootLayout.width
                            layoutParams.height = rootLayout.height
                            transitionButton.layoutParams = layoutParams

                            transitionButton.stopAnimation(
                                TransitionButton.StopAnimationStyle.EXPAND
                            ) {

                                startActivity(
                                    Intent(this, landingWIthFragmentActivity::class.java).apply {
                                        putExtra("uid", uid)
                                        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                    }
                                )
                                finish()
                            }
                        }
                        .addOnFailureListener {
                            CuteToast.ct(this, "Login failed. Please check your credentials and try again.", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
                            Log.e("AUTH", "Login error: ${it.message}")
                            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                        }
                } else {

                    CuteToast.ct(this, "User not found with that username", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
                    transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }
            }
            .addOnFailureListener {
                CuteToast.ct(this, "Error fetching user dat", CuteToast.LENGTH_SHORT, CuteToast.SAD, true).show();
                Log.e("Firebase Error", "Error: ${it.message}")

                transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
            }
    }

}
