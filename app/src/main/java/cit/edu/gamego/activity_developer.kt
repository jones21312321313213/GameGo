package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageView

class activity_developer : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer);

        val abme_archival = findViewById<Button>(R.id.about_me_archival);
        val abme_arco = findViewById<Button>(R.id.about_me_arco);
        val back = findViewById<ImageView>(R.id  .abme_devs_back);
        abme_archival.setOnClickListener {
            startActivity(
                Intent(this,about_me_dev1::class.java)
            )
        }
        abme_arco.setOnClickListener {
            startActivity(
                Intent(this,about_me_dev2::class.java)
            )
        }
        // back
        back.setOnClickListener{
            startActivity(
                Intent(this,activity_landing::class.java)
            )
        }
    }
}