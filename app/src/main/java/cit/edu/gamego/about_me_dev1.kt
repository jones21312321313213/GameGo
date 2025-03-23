package cit.edu.gamego

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class about_me_dev1 : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me_dev1);


        val abmetv = findViewById<TextView>(R.id.abmetv1)
        val message = "Hello I am a developer from cebu you can call me ______"
        abmetv.text = message;


        val back = findViewById<ImageView>(R.id.abme_dev1_back);

        back.setOnClickListener {
            finish()
        }

    }
}