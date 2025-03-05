package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_landing : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing);

        val btnAccount = findViewById<ImageView>(R.id.account_Id);
        btnAccount.setOnClickListener{
            val intent = Intent(this,ProfilePictureActivity::class.java);
            startActivity(intent);
        }
    }
}