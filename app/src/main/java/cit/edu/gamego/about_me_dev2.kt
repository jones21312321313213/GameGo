package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class about_me_dev2 : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me_dev2);


        val abmetv = findViewById<TextView>(R.id.abmetv2)
        val message = "Hello I am a developer from cebu you can call me ______"
        abmetv.text = message;


        val back = findViewById<ImageView>(R.id.abme_dev2_back);

        back.setOnClickListener{
            finish()
        }
    }
}