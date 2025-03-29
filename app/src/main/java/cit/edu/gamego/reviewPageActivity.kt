package cit.edu.gamego

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import cit.edu.gamego.data.Game
import cit.edu.gamego.extensions.setupAndLoad

class reviewPageActivity : Activity() {

    private lateinit var webView: WebView  // ✅ Declare webView properly

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_page)

        val gameImg = findViewById<ImageView>(R.id.game_pic_rp)
        val gameTitle = findViewById<TextView>(R.id.game_title_rp)
        val gameRev = findViewById<TextView>(R.id.review_content)
        val back = findViewById<ImageView>(R.id.back_rp)
        var trailer: String = ""

        intent?.let {
            it.getStringExtra("title")?.let { title ->
                gameTitle.text = title
            }
            it.getIntExtra("imageRes", 0).let { imageResId ->
                if (imageResId != 0) {
                    gameImg.setImageResource(imageResId)
                }
            }
            trailer = it.getStringExtra("trailer") ?: ""  // ✅ Prevents "null" string
        }


        // ✅ Initialize WebView
        webView = findViewById(R.id.game_webview)
        webView.setupAndLoad(trailer)

        // ✅ Set game review text based on title
        gameRev.text = when (title.toString().lowercase()) { 
            "the ye quest" -> "This game explores how Kanye takes inspiration from controversial figures to make music."
            "black myth wukong 2024" -> "An action RPG where players control a monkey warrior inspired by Journey to the West."
            "monster hunter: world 2018" -> "Embark on the ultimate hunting experience, tracking and battling massive creatures."
            "helldivers 2 2022" -> "A cooperative third-person shooter where players defend Super Earth from alien threats."
            else -> "No review available."
        }

        // ✅ Back button functionality
        back.setOnClickListener {
            finish()
        }

        // ✅ Heart button (Favorites)
        val heart = findViewById<ImageView>(R.id.heart)
        val imageResId = gameImg.tag as? Int ?: R.drawable.aaa

        heart.setOnClickListener {
            startActivity(
                Intent(this, Favorites::class.java).apply {
                    putExtra("title", gameTitle.text.toString())
                    putExtra("releaseDate", "21")
                    putExtra("imageRes", imageResId)
                }
            )
        }
    }
}
