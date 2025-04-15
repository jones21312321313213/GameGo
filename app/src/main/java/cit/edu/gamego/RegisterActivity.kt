package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cit.edu.gamego.extensions.isTextNullOrEmpty
import cit.edu.gamego.extensions.toast

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_f3);

        val button_signUp = findViewById<TextView>(R.id.register_signup_Id);
        button_signUp.setOnClickListener {
            finish()
        }

        val username= findViewById<EditText>(R.id.register_username_Id);
        val password= findViewById<EditText>(R.id.register_pass_Id);
        val email = findViewById<EditText>(R.id.register_email_Id)
        val cpass = findViewById<EditText>(R.id.register_confirmpass_Id)
        val btnRegister = findViewById<Button>(R.id.register_Id);



        btnRegister.setOnClickListener{
            if (username.isTextNullOrEmpty()
                || password.isTextNullOrEmpty() || email.isTextNullOrEmpty() ||
                cpass.isTextNullOrEmpty()){
                toast("fill out the form completely");
                return@setOnClickListener
            }

            if(cpass.text.toString() != password.text.toString()){
                toast("passwords does not match")
                return@setOnClickListener
            }

            startActivity(
                Intent(this, LoginActivity::class.java).apply {
                    putExtra("username", username.text.toString())
                    putExtra("password", password.text.toString())
                    putExtra("email",email.text.toString())
                    toast("success")
                }
            )
            finish()
        }

    }

}