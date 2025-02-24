package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfilePictureActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture);

        val button_logout = findViewById<Button>(R.id.logout_Id);
        button_logout.setOnClickListener {
            Log.e("CSIT 284", "absolute cinema");
            Toast.makeText(this,"avcde", Toast.LENGTH_LONG).show();
            val intent = Intent(this,LoginActivity::class.java);
            startActivity(intent);
        }

        // should return to the main page but wala paman so go to login page (this is temp)
        val button_profilepic_back = findViewById<ImageView>(R.id.profilepic_back_Id);
        button_profilepic_back.setOnClickListener{
            Log.e("Dont u", "absolute cinema");
            Toast.makeText(this,"avasd",Toast.LENGTH_LONG).show();
            val intent = Intent(this,LoginActivity::class.java);
            startActivity(intent);
        }
    }
}