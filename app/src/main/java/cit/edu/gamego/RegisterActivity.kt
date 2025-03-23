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

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_f3);

        val button_signUp = findViewById<TextView>(R.id.register_signup_Id);
        button_signUp.setOnClickListener {
            Log.e("CSIT 284", "absolute cinema");
            Toast.makeText(this,"Success", Toast.LENGTH_LONG).show();
            val intent = Intent(this,LoginActivity::class.java);//temp
            startActivity(intent);
        }

        val username= findViewById<EditText>(R.id.register_username_Id);
        val password= findViewById<EditText>(R.id.register_pass_Id);

        val btnRegister = findViewById<Button>(R.id.register_Id);
        btnRegister.setOnClickListener{
            if (username.text.toString().isNullOrEmpty()
                || password.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Fill out the form completely.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            startActivity(
                Intent(this, LoginActivity::class.java).apply {
                    putExtra("username", username.text.toString())
                    putExtra("password", password.text.toString())
                }
            )
        }

    }

}