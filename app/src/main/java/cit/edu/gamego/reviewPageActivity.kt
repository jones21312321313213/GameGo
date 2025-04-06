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
import com.bumptech.glide.Glide

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
        val rating = findViewById<TextView>(R.id.ratings_tv_rp)
        var trailer: String = ""

        var abc: String? = "a"
        intent?.let {
            it.getStringExtra("title")?.let { title ->
                gameTitle.text = title
            }
            it.getStringExtra("imageReswGlide").let { imageReswGlide ->
                Glide.with(this).load(imageReswGlide).into(gameImg)
                abc = imageReswGlide
            }
            it.getStringExtra("imageRes")?.let{imageRes->
                gameImg.setImageResource(imageRes.toInt())
                abc = imageRes
            }

            it.getDoubleExtra("ratings",0.0).let { ratings ->
                rating.text = ratings.toString()
            }
            trailer = it.getStringExtra("trailer") ?: ""  // ✅ Prevents "null" string
        }


        // ✅ Initialize WebView
        webView = findViewById(R.id.game_webview)
        webView.setupAndLoad(trailer)

        // temp
        gameRev.text = when {
            gameTitle.text.toString().trim().equals("Ye Quest", ignoreCase = true) ->
                "This game explores how Kanye takes inspiration from controversial figures to make music."
            gameTitle.text.toString().trim().equals("Black Myth Wukong", ignoreCase = true) ->
                "An action RPG where players control a monkey warrior inspired by Journey to the West."
            gameTitle.text.toString().trim().equals("Monster Hunter: World", ignoreCase = true) ->
                "Embark on the ultimate hunting experience, tracking and battling massive creatures."
            gameTitle.text.toString().trim().equals("Helldivers 2", ignoreCase = true) ->
                "A cooperative third-person shooter where players defend Super Earth from alien threats."
            gameTitle.text.toString().trim().equals("DOTA 2", ignoreCase = true) ->
                "A competitive MOBA where two teams of five heroes battle to destroy the enemy’s Ancient."
            gameTitle.text.toString().trim().equals("League of Legends", ignoreCase = true) ->
                "A fast-paced MOBA where champions with unique abilities fight to control Summoner’s Rift."
            gameTitle.text.toString().trim().equals("Counter Strike 2", ignoreCase = true) ->
                "A tactical first-person shooter where teams compete in bomb defusal and hostage rescue modes."
            gameTitle.text.toString().trim().equals("God of War: Ragnarok", ignoreCase = true) ->
                "A mythological action-adventure game following Kratos and Atreus on their Norse journey."
            gameTitle.text.toString().trim().equals("Valorant", ignoreCase = true) ->
                "A tactical shooter where agents with unique abilities battle in intense 5v5 matches."
            gameTitle.text.toString().trim().equals("Elden Ring", ignoreCase = true) ->
                "An open-world action RPG where players explore the Lands Between and battle formidable foes."
            else -> "No review available."
        }

        back.setOnClickListener {
            finish()
        }

        val heart = findViewById<ImageView>(R.id.heart)

        heart.setOnClickListener {
            startActivity(
                Intent(this, Favorites::class.java).apply {
                    putExtra("title", gameTitle.text.toString())
                    putExtra("releaseDate", "21")
                    putExtra("imageRes", abc)
                    putExtra("rating",rating.text.toString())
                }
            )
        }
    }
}