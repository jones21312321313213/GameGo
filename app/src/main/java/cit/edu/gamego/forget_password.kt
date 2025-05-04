package cit.edu.gamego


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.FirebaseDatabase
import com.rejowan.cutetoast.CuteToast

class forget_password : Activity() {
    private lateinit var cAuth: FirebaseAuth
    private lateinit var userEmailInput: EditText
    private lateinit var submit: Button
    private lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        // Firebase Auth instance
        cAuth = FirebaseAuth.getInstance()

        // UI references
        userEmailInput = findViewById(R.id.login_code)
        submit = findViewById(R.id.submit)
        back = findViewById(R.id.back)

        submit.setOnClickListener {
            val email = userEmailInput.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                CuteToast.ct(this, "Please enter your email", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                return@setOnClickListener
            }

            cAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        CuteToast.ct(this, "We have sent the email", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        CuteToast.ct(this, "Invalid format", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                    }
                }
        }

        back.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}