package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView

class GameConfigActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_config)

        val back = findViewById<ImageView>(R.id.settings_back_Id)


        back.setOnClickListener{
            startActivity(
                Intent(this,settingsFragment::class.java)
            )
            finish()
        }
    }
}