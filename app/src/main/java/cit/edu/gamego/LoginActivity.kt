package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_f3);

        val et_username = findViewById<EditText>(R.id.login_username_Id);
        val et_password = findViewById<EditText>(R.id.login_pass_Id);
        var et_email: String? = null
        intent?.let {
            it.getStringExtra("username")?.let {username ->
                et_username.setText(username)
            }

            it.getStringExtra("password")?.let {password ->
                et_password.setText(password)
            }

            it.getStringExtra("email")?.let{
                et_email = it
            }
        }

        val button_register = findViewById<TextView>(R.id.login_signup_Id);
        button_register.setOnClickListener {
            Log.e("CSIT 284", "absolute cinema");
            val intent = Intent(this,RegisterActivity::class.java);//temp
            startActivity(intent);
        }

        val btnMain = findViewById<Button>(R.id.login_Id);
        btnMain.setOnClickListener {
            if (et_username.text.toString().isNullOrEmpty()|| et_password.text.toString().isNullOrEmpty()) {
                Toast.makeText(this,"Username and pass is empty",Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }
            startActivity(
                Intent(this,landingWIthFragmentActivity::class.java).apply {
                    putExtra("username", et_username.text.toString());
                    putExtra("password", et_password.text.toString());
                    putExtra("email", et_email);
                }
            )
        }

    }
}