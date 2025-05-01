package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class forget_password : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)



        val back = findViewById<Button>(R.id.back)
        val submit = findViewById<Button>(R.id.submit)

        back.setOnClickListener{
            startActivity(
                Intent(this,LoginActivity::class.java)
            )
            finish()
        }

        submit.setOnClickListener{
            startActivity(
                Intent(this,new_password::class.java)
            )
            finish()
        }


    }
}