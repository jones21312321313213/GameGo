package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class settings : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
//        val back = findViewById<ImageView>(R.id.settings_back_Id);
//
//        back.setOnClickListener{
//            startActivity(
//                Intent(this,activity_landing::class.java)
//            )
//        }
    }
}