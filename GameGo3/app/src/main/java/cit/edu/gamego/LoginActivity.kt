package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_f3);

        val button_register = findViewById<Button>(R.id.login_Id);
        button_register.setOnClickListener {
            Log.e("CSIT 284", "absolute cinema");
            Toast.makeText(this,"absolute dogshit",Toast.LENGTH_LONG).show();
            val intent = Intent(this,RegisterActivity::class.java);
            startActivity(intent);
        }
    }
}